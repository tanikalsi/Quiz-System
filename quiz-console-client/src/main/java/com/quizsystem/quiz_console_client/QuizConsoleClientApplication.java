package com.quizsystem.quiz_console_client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class QuizConsoleClientApplication implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser = null;
    private String jwtToken = null;

    private static final String API_GATEWAY_URL = "http://localhost:8080";
    private static final String USER_SERVICE_URL = API_GATEWAY_URL + "/USER-SERVICE/api/users";
    private static final String QUIZ_SERVICE_URL = API_GATEWAY_URL + "/QUIZ-SERVICE";

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(QuizConsoleClientApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else if ("TEACHER".equalsIgnoreCase(currentUser.getRole())) {
                showTeacherMenu();
            } else if ("STUDENT".equalsIgnoreCase(currentUser.getRole())) {
                showStudentMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n--- Welcome to the Quiz System ---");
        System.out.println("1. Login");
        System.out.println("2. Create a new User");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> login();
            case 2 -> createUser();
            case 3 -> {
                System.out.println("Exiting application...");
                System.exit(0);
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void showTeacherMenu() {
        System.out.println("\n--- Teacher Menu (Logged in as: " + currentUser.getUsername() + ") ---");
        System.out.println("1. Create a new Quiz");
        System.out.println("2. Add a Question to a Quiz");
        System.out.println("3. List all Quizzes");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> createQuiz();
            case 2 -> addQuestion();
            case 3 -> listQuizzes();
            case 4 -> logout();
            default -> System.out.println("Invalid choice.");
        }
    }

    private void showStudentMenu() {
        System.out.println("\n--- Student Menu (Logged in as: " + currentUser.getUsername() + ") ---");
        System.out.println("1. View all Quizzes");
        System.out.println("2. Take a Quiz");
        System.out.println("3. View My Scores");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> listQuizzes();
            case 2 -> takeQuiz();
            case 3 -> viewMyScores();
            case 4 -> logout();
            default -> System.out.println("Invalid choice.");
        }
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        try {
            AuthenticationResponse authResponse = restTemplate.postForObject(USER_SERVICE_URL + "/login", loginRequest, AuthenticationResponse.class);
            if (authResponse != null && authResponse.getJwt() != null) {
                jwtToken = authResponse.getJwt();

                // ** THIS IS THE KEY CHANGE **
                // After getting the token, call the new /me endpoint to get full user details
                HttpEntity<Void> entity = getHttpEntityForGet();
                ResponseEntity<User> userResponse = restTemplate.exchange(USER_SERVICE_URL + "/me", HttpMethod.GET, entity, User.class);
                currentUser = userResponse.getBody();

                if (currentUser != null) {
                    System.out.println("Login successful! Welcome " + currentUser.getUsername());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: Invalid username or password. " + e.getMessage());
        }
    }

    private void logout() {
        currentUser = null;
        jwtToken = null;
        System.out.println("You have been logged out successfully.");
    }

    private void createUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (TEACHER/STUDENT): ");
        String role = scanner.nextLine().toUpperCase();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        try {
            User createdUser = restTemplate.postForObject(USER_SERVICE_URL, user, User.class);
            System.out.println("User created successfully with ID: " + createdUser.getId());
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private HttpEntity<Object> getHttpEntityWithAuthHeader(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        if (jwtToken != null) {
            headers.set("Authorization", "Bearer " + jwtToken);
        }
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity<Void> getHttpEntityForGet() {
        HttpHeaders headers = new HttpHeaders();
        if (jwtToken != null) {
            headers.set("Authorization", "Bearer " + jwtToken);
        }
        return new HttpEntity<>(headers);
    }

    private void createQuiz() {
        System.out.print("Enter quiz title: ");
        String title = scanner.nextLine();
        System.out.print("Enter quiz description: ");
        String description = scanner.nextLine();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setUserId(currentUser.getId()); // This now works

        try {
            HttpEntity<Object> entity = getHttpEntityWithAuthHeader(quiz);
            Quiz createdQuiz = restTemplate.postForObject(QUIZ_SERVICE_URL + "/quiz", entity, Quiz.class);
            System.out.println("Quiz created successfully! ID: " + createdQuiz.getId());
        } catch (Exception e) {
            System.out.println("Error creating quiz: " + e.getMessage());
        }
    }

    private void addQuestion() {
        System.out.print("Enter Quiz ID to add a question to: ");
        long quizId = Long.parseLong(scanner.nextLine());

        Question question = new Question();
        System.out.print("Enter question text: ");
        question.setQuestionText(scanner.nextLine());
        System.out.print("Enter Option 1: ");
        question.setOption1(scanner.nextLine());
        System.out.print("Enter Option 2: ");
        question.setOption2(scanner.nextLine());
        System.out.print("Enter Option 3: ");
        question.setOption3(scanner.nextLine());
        System.out.print("Enter Option 4: ");
        question.setOption4(scanner.nextLine());
        System.out.print("Enter Correct Answer (the text of the correct option): ");
        question.setCorrectAnswer(scanner.nextLine());

        String url = QUIZ_SERVICE_URL + "/quiz/" + quizId + "/questions";
        try {
            HttpEntity<Object> entity = getHttpEntityWithAuthHeader(question);
            Question createdQuestion = restTemplate.postForObject(url, entity, Question.class);
            System.out.println("Question added successfully! ID: " + createdQuestion.getId());
        } catch (Exception e) {
            System.out.println("Error adding question: " + e.getMessage());
        }
    }

    private void listQuizzes() {
        try {
            Quiz[] quizzes = restTemplate.getForObject(QUIZ_SERVICE_URL + "/quiz", Quiz[].class);
            System.out.println("\n--- Available Quizzes ---");
            if (quizzes != null && quizzes.length > 0) {
                for (Quiz q : quizzes) {
                    System.out.println("ID: " + q.getId() + ", Title: " + q.getTitle() + ", Description: " + q.getDescription());
                }
            } else {
                System.out.println("No quizzes available.");
            }
        } catch (Exception e) {
            System.out.println("Error fetching quizzes: " + e.getMessage());
        }
    }

    private void takeQuiz() {
        System.out.print("Enter the ID of the quiz you want to take: ");
        long quizId = Long.parseLong(scanner.nextLine());

        try {
            String questionsUrl = QUIZ_SERVICE_URL + "/quiz/" + quizId + "/questions";
            ResponseEntity<Question[]> response = restTemplate.exchange(questionsUrl, HttpMethod.GET, getHttpEntityForGet(), Question[].class);
            Question[] questions = response.getBody();

            if (questions == null || questions.length == 0) {
                System.out.println("This quiz has no questions or does not exist.");
                return;
            }

            Map<Long, String> answers = new HashMap<>();
            for (Question q : questions) {
                System.out.println("\nQ: " + q.getQuestionText());
                System.out.println("  1. " + q.getOption1());
                System.out.println("  2. " + q.getOption2());
                System.out.println("  3. " + q.getOption3());
                System.out.println("  4. " + q.getOption4());
                System.out.print("Your answer (enter the text of the option): ");
                String answer = scanner.nextLine();
                answers.put(q.getId(), answer);
            }

            Map<String, Object> submission = new HashMap<>();
            submission.put("userId", currentUser.getId()); // This now works
            submission.put("answers", answers);

            String submitUrl = QUIZ_SERVICE_URL + "/quiz/" + quizId + "/submit";
            HttpEntity<Object> submissionEntity = getHttpEntityWithAuthHeader(submission);
            Result result = restTemplate.postForObject(submitUrl, submissionEntity, Result.class);
            System.out.println("\nQuiz Submitted! Your score: " + result.getScore() + "/" + result.getTotalQuestions());

        } catch (Exception e) {
            System.out.println("Error while taking the quiz: " + e.getMessage());
        }
    }

    private void viewMyScores() {
        try {
            String url = QUIZ_SERVICE_URL + "/results/user/" + currentUser.getId(); // This now works
            ResponseEntity<Result[]> response = restTemplate.exchange(url, HttpMethod.GET, getHttpEntityForGet(), Result[].class);
            Result[] results = response.getBody();

            if (results == null || results.length == 0) {
                System.out.println("You have not attempted any quizzes yet.");
                return;
            }

            System.out.println("\n--- Your Past Scores ---");
            for (Result r : results) {
                System.out.println(r.toString());
            }
        } catch (Exception e) {
            System.out.println("Error fetching your scores: " + e.getMessage());
        }
    }
}