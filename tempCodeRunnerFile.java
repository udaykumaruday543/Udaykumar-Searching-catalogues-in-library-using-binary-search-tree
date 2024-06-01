import java.util.*;

class BSTNode {
    String bookName;
    BSTNode left, right;

    public BSTNode(String bookName) {
        this.bookName = bookName;
        this.left = this.right = null;
    }
}

class BinarySearchTree {
    private BSTNode root;

    public BinarySearchTree() {
        root = null;
    }

    // Insert a book into the BST
    public void insert(String bookName) {
        root = insertRec(root, bookName);
    }

    private BSTNode insertRec(BSTNode root, String bookName) {
        if (root == null) {
            root = new BSTNode(bookName);
            return root;
        }
        if (bookName.compareTo(root.bookName) < 0)
            root.left = insertRec(root.left, bookName);
        else if (bookName.compareTo(root.bookName) > 0)
            root.right = insertRec(root.right, bookName);
        return root;
    }

    // Delete a book from the BST
    public void delete(String bookName) {
        root = deleteRec(root, bookName);
    }

    private BSTNode deleteRec(BSTNode root, String bookName) {
        if (root == null) return root;

        if (bookName.compareTo(root.bookName) < 0)
            root.left = deleteRec(root.left, bookName);
        else if (bookName.compareTo(root.bookName) > 0)
            root.right = deleteRec(root.right, bookName);
        else {
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            root.bookName = minValue(root.right);
            root.right = deleteRec(root.right, root.bookName);
        }

        return root;
    }

    private String minValue(BSTNode root) {
        String minv = root.bookName;
        while (root.left != null) {
            minv = root.left.bookName;
            root = root.left;
        }
        return minv;
    }

    // Search for a book in the BST
    public boolean search(String bookName) {
        return searchRec(root, bookName);
    }

    private boolean searchRec(BSTNode root, String bookName) {
        if (root == null) return false;
        if (root.bookName.equals(bookName)) return true;
        if (root.bookName.compareTo(bookName) > 0)
            return searchRec(root.left, bookName);
        return searchRec(root.right, bookName);
    }

    // In-order traversal to print the BST
    public void inOrder() {
        inOrderRec(root);
    }

    private void inOrderRec(BSTNode root) {
        if (root != null) {
            inOrderRec(root.left);
            System.out.println(root.bookName);
            inOrderRec(root.right);
        }
    }
}

class Library {
    private BinarySearchTree bst;
    private HashMap<Integer, String> bookMap; // Map unique book ID to book name
    private int[][] bookRecords; // 2D array to store book quantities
    private HashMap<Integer, List<String>> studentBooks; // Track books issued to students

    public Library(int maxBooks) {
        bst = new BinarySearchTree();
        bookMap = new HashMap<>();
        bookRecords = new int[maxBooks][2]; // [][0] = total quantity, [][1] = available quantity
        studentBooks = new HashMap<>();
    }

    public void adminLogin(String username, String password) {
        if (username.equals("admin") && password.equals("password")) {
            System.out.println("Admin logged in successfully.");
        } else {
            System.out.println("Invalid login credentials.");
        }
    }

    public void addBook(int id, String bookName, int quantity) {
        bst.insert(bookName);
        bookMap.put(id, bookName);
        bookRecords[id][0] = quantity;
        bookRecords[id][1] = quantity;
        System.out.println("Book added successfully.");
    }

    public void deleteBook(int id) {
        String bookName = bookMap.get(id);
        if (bookName != null) {
            bst.delete(bookName);
            bookMap.remove(id);
            bookRecords[id][0] = 0;
            bookRecords[id][1] = 0;
            System.out.println("Book deleted successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    public void updateBookQuantity(int id, int newQuantity) {
        if (bookMap.containsKey(id)) {
            bookRecords[id][0] = newQuantity;
            bookRecords[id][1] = newQuantity; // Assuming all books are available initially
            System.out.println("Book quantity updated successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    public void listAllBooks() {
        System.out.println("Listing all books:");
        for (int id : bookMap.keySet()) {
            String bookName = bookMap.get(id);
            int totalQty = bookRecords[id][0];
            int availableQty = bookRecords[id][1];
            System.out.println("ID: " + id + ", Name: " + bookName + ", Total: " + totalQty + ", Available: " + availableQty);
        }
    }

    public void printBooksInOrder() {
        System.out.println("Books in ascending order:");
        bst.inOrder();
    }

    public void studentLogin(int studentId) {
        if (!studentBooks.containsKey(studentId)) {
            studentBooks.put(studentId, new ArrayList<>());
        }
        System.out.println("Student with ID: " + studentId + " logged in successfully.");
    }

    public void issueBook(int studentId, int bookId) {
        if (studentBooks.get(studentId).size() >= 2) {
            System.out.println("Cannot issue more than 2 books simultaneously.");
            return;
        }
        String bookName = bookMap.get(bookId);
        if (bookName == null) {
            System.out.println("Book is not available in the library.");
        } else if (bookRecords[bookId][1] <= 0) {
            System.out.println("This book is currently unavailable. Please try after some days.");
        } else {
            bookRecords[bookId][1]--;
            studentBooks.get(studentId).add(bookName);
            System.out.println("Book issued successfully.");
        }
    }

    public void returnBook(int studentId, int bookId) {
        String bookName = bookMap.get(bookId);
        if (bookName != null && studentBooks.get(studentId).contains(bookName)) {
            bookRecords[bookId][1]++;
            studentBooks.get(studentId).remove(bookName);
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Invalid book ID or book not issued to student.");
        }
    }
}

public class tempCodeRunnerFile {
    public static void main(String[] args) {
        Library library = new Library(100); // Assuming max 100 books

        // Admin operations
        library.adminLogin("admin", "password");
        library.addBook(1, "Introduction to Algorithms", 10);
        library.addBook(2, "Design Patterns", 5);
        library.addBook(3, "Effective Java", 8);

        library.listAllBooks();
        library.printBooksInOrder();

        library.updateBookQuantity(2, 7);
        library.deleteBook(3);

        library.listAllBooks();
        library.printBooksInOrder();

        // Student operations
        library.studentLogin(101);
        library.issueBook(101, 1);
        library.issueBook(101, 2);
        library.issueBook(101, 2); // Trying to issue more than 2 books
        library.returnBook(101, 1);

        library.listAllBooks();
    }
}
