package com.quizsystem;

import com.quizsystem.model.Question;
import com.quizsystem.model.Quiz;
import com.quizsystem.model.User;
import com.quizsystem.service.QuizService;
import com.quizsystem.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {

    private static QuizService quizService;
    private static UserService userService;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("quiz-config.xml");
        quizService = context.getBean("quizService", QuizService.class);
        userService = context.getBean("userService", UserService.class);

        Scanner scanner = new Scanner(System.in);

        // --- Main Login Loop (Unchanged) ---
        while (true) {
            System.out.println("\n--- WELCOME TO THE QUIZ SYSTEM ---");
            System.out.print("Please enter your username (or type 'exit' to quit): ");
            String username = scanner.nextLine();

            if ("exit".equalsIgnoreCase(username)) {
                break;
            }

            User currentUser = userService.findUserByUsername(username);

            if (currentUser == null) {
                System.out.println("User not found. Let's register you.");
                System.out.print("Choose a role (TEACHER / STUDENT): ");
                String role = scanner.nextLine().toUpperCase();

                if ("TEACHER".equals(role) || "STUDENT".equals(role)) {
                    userService.createUser(username, role);
                    currentUser = userService.findUserByUsername(username);
                } else {
                    System.out.println("Invalid role. Please try again.");
                    continue;
                }
            }

            System.out.println("Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            if ("TEACHER".equals(currentUser.getRole())) {
                showTeacherMenu(scanner);
            } else if ("STUDENT".equals(currentUser.getRole())) {
                showStudentMenu(scanner);
            }
        }

        System.out.println("Exiting application. Goodbye!");
        scanner.close();
    }

    private static void showTeacherMenu(Scanner scanner) {
        // --- This method now handles creating multiple-choice questions ---
        while (true) {
            System.out.println("\n--- TEACHER MENU ---");
            System.out.println("1. Create a New Quiz (with questions)");
            System.out.println("2. Add a Question to an Existing Quiz");
            System.out.println("3. View All Quizzes");
            System.out.println("4. Logout");
            System.out.print("Please enter your choice: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Invalid input.");
            }
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter the title for the new quiz: ");
                String quizTitle = scanner.nextLine();
                System.out.print("How many questions do you want to add? ");
                int questionCount = scanner.nextInt();
                scanner.nextLine();

                Quiz newQuiz = new Quiz();
                newQuiz.setTitle(quizTitle);
                newQuiz = quizService.createQuiz(newQuiz);

                for (int i = 1; i <= questionCount; i++) {
                    System.out.println("\n--- Adding Question " + i + "/" + questionCount + " ---");
                    addMultipleChoiceQuestion(scanner, newQuiz.getId());
                }
                System.out.println("\nSUCCESS: Quiz '" + quizTitle + "' with " + questionCount + " questions has been created!");

            } else if (choice == 2) {
                List<Quiz> quizzes = quizService.getAllQuizzes();
                if (quizzes.isEmpty()) {
                    System.out.println("No quizzes available. Please create one first.");
                    continue;
                }
                quizzes.forEach(q -> System.out.println("ID: " + q.getId() + ", Title: " + q.getTitle()));
                System.out.print("Enter the ID of the quiz: ");
                long quizId = scanner.nextLong();
                scanner.nextLine();
                addMultipleChoiceQuestion(scanner, quizId);
            } else if (choice == 3) {
                List<Quiz> quizzes = quizService.getAllQuizzes();
                if (quizzes.isEmpty()) {
                    System.out.println("No quizzes found.");
                } else {
                    System.out.println("--- All Quizzes ---");
                    quizzes.forEach(q -> System.out.println("ID: " + q.getId() + ", Title: " + q.getTitle()));
                }
            } else if (choice == 4) {
                System.out.println("Logging out...");
                return;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    // Helper method to avoid repeating code for adding questions
    private static void addMultipleChoiceQuestion(Scanner scanner, Long quizId) {
        System.out.print("Enter the question text: ");
        String qText = scanner.nextLine();
        System.out.print("Enter option A: ");
        String optA = scanner.nextLine();
        System.out.print("Enter option B: ");
        String optB = scanner.nextLine();
        System.out.print("Enter option C: ");
        String optC = scanner.nextLine();
        System.out.print("Enter option D: ");
        String optD = scanner.nextLine();
        System.out.print("Which option is correct (A, B, C, or D)? ");
        String cAnswer = scanner.nextLine().toUpperCase();

        Question newQ = new Question();
        newQ.setQuestionText(qText);
        newQ.setOptionA(optA);
        newQ.setOptionB(optB);
        newQ.setOptionC(optC);
        newQ.setOptionD(optD);
        newQ.setCorrectAnswer(cAnswer);

        quizService.addQuestionToQuiz(quizId, newQ);
        System.out.println("SUCCESS: Question added.");
    }

    private static void showStudentMenu(Scanner scanner) {
        // --- This method now displays options and checks the student's choice ---
        while(true) {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("1. Take a Quiz");
            System.out.println("2. Logout");
            System.out.print("Please enter your choice: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Invalid input.");
            }
            scanner.nextLine();

            if(choice == 1) {
                List<Quiz> quizzes = quizService.getAllQuizzes();
                if (quizzes.isEmpty()) {
                    System.out.println("Sorry, there are no quizzes available.");
                    continue;
                }
                System.out.println("--- Available Quizzes ---");
                quizzes.forEach(q -> System.out.println("ID: " + q.getId() + ", Title: " + q.getTitle()));
                System.out.print("Enter the ID of the quiz you want to take: ");

                long quizId = -1;
                try {
                    quizId = scanner.nextLong();
                } catch (InputMismatchException e) {
                    System.out.println("ERROR: Invalid ID.");
                }
                scanner.nextLine();
                if (quizId == -1) continue;

                Quiz quizToTake = quizService.getQuizWithQuestions(quizId);
                if (quizToTake == null || quizToTake.getQuestions().isEmpty()) {
                    System.out.println("Quiz not found or has no questions.");
                    continue;
                }

                int score = 0;
                System.out.println("\n--- Starting Quiz: " + quizToTake.getTitle() + " ---");
                for (Question question : quizToTake.getQuestions()) {
                    System.out.println("\nQuestion: " + question.getQuestionText());
                    System.out.println("  A) " + question.getOptionA());
                    System.out.println("  B) " + question.getOptionB());
                    System.out.println("  C) " + question.getOptionC());
                    System.out.println("  D) " + question.getOptionD());
                    System.out.print("Your choice (A, B, C, or D): ");
                    String answer = scanner.nextLine().toUpperCase();

                    if (answer.equals(question.getCorrectAnswer())) {
                        score++;
                        System.out.println("Correct!");
                    } else {
                        System.out.println("Incorrect. The correct answer was: " + question.getCorrectAnswer());
                    }
                }
                System.out.println("\n--- Quiz Complete ---");
                System.out.println("Your final score is: " + score + "/" + quizToTake.getQuestions().size());

            } else if(choice == 2) {
                System.out.println("Logging out...");
                return;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}