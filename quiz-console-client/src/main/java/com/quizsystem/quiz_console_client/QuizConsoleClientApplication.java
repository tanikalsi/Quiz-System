package com.quizsystem.console_client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class QuizConsoleClientApplication implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(QuizConsoleClientApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE); // disable embedded web server
        app.run(args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Quiz Application Menu ---");
            System.out.println("1. Create a new User");
            System.out.println("2. Create a new Quiz");
            System.out.println("3. Add a Question to a Quiz");
            System.out.println("4. List all Quizzes");
            System.out.println("5. List all Questions of a Quiz");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createUser(scanner);
                case 2 -> createQuiz(scanner);
                case 3 -> addQuestion(scanner);
                case 4 -> listQuizzes();
                case 5 -> listQuestions(scanner);
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role: ");
        String role = scanner.nextLine();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        String url = "http://localhost:8080/api/users";
        try {
            User createdUser = restTemplate.postForObject(url, user, User.class);
            System.out.println("User created with ID: " + createdUser.getId());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createQuiz(Scanner scanner) {
        System.out.print("Enter quiz title: ");
        String title = scanner.nextLine();
        System.out.print("Enter quiz description: ");
        String description = scanner.nextLine();
        System.out.print("Enter created by: ");
        String createdBy = scanner.nextLine();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setCreatedBy(createdBy);

        String url = "http://localhost:8080/quiz";
        try {
            Quiz createdQuiz = restTemplate.postForObject(url, quiz, Quiz.class);
            System.out.println("Quiz created successfully! ID: " + createdQuiz.getId());
        } catch (Exception e) {
            System.out.println("Error creating quiz: " + e.getMessage());
        }
    }

    private void addQuestion(Scanner scanner) {
        System.out.print("Enter Quiz ID to add question: ");
        Long quizId = scanner.nextLong();
        scanner.nextLine();

        Question question = new Question();
        System.out.print("Enter question text: ");
        question.setQuestionText(scanner.nextLine());
        System.out.print("Option 1: ");
        question.setOption1(scanner.nextLine());
        System.out.print("Option 2: ");
        question.setOption2(scanner.nextLine());
        System.out.print("Option 3: ");
        question.setOption3(scanner.nextLine());
        System.out.print("Option 4: ");
        question.setOption4(scanner.nextLine());
        System.out.print("Correct Answer (1-4): ");
        question.setCorrectAnswer(scanner.nextLine());

        String url = "http://localhost:8080/quiz/" + quizId + "/questions";
        try {
            Question createdQuestion = restTemplate.postForObject(url, question, Question.class);
            System.out.println("Question added successfully! ID: " + createdQuestion.getId());
        } catch (Exception e) {
            System.out.println("Error adding question: " + e.getMessage());
        }
    }

    private void listQuizzes() {
        String url = "http://localhost:8080/quiz";
        try {
            Quiz[] quizzes = restTemplate.getForObject(url, Quiz[].class);
            System.out.println("\n--- Quizzes ---");
            for (Quiz q : quizzes) {
                System.out.println("ID: " + q.getId() + ", Title: " + q.getTitle() + ", Created By: " + q.getCreatedBy());
            }
        } catch (Exception e) {
            System.out.println("Error fetching quizzes: " + e.getMessage());
        }
    }

    private void listQuestions(Scanner scanner) {
        System.out.print("Enter Quiz ID to list questions: ");
        Long quizId = scanner.nextLong();
        scanner.nextLine();

        String url = "http://localhost:8080/quiz/" + quizId + "/questions";
        try {
            Question[] questions = restTemplate.getForObject(url, Question[].class);
            System.out.println("\n--- Questions ---");
            for (Question q : questions) {
                System.out.println("ID: " + q.getId() + ", Text: " + q.getQuestionText());
            }
        } catch (Exception e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }
    }
}
