import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class LibraryManagementSystem1 extends JFrame {


    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    
    JTextField txtBookId, txtTitle, txtAuthor, txtUserId, txtName, txtEmail;
    JTable table;
    DefaultTableModel model;


    public LibraryManagementSystem1() {
        connect();
        initComponents();
    }


    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/librarydb", "root", "arjun");
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed");
        }
    }

   
    private void initComponents() {
        setTitle("Library Management System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

       
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5, 10, 10));

        JButton btnAddBook = new JButton("Add Book");
        JButton btnSearchBook = new JButton("Search Book");
        JButton btnBorrowBook = new JButton("Borrow Book");
        JButton btnReturnBook = new JButton("Return Book");
        JButton btnViewHistory = new JButton("View History");

        buttonPanel.add(btnAddBook);
        buttonPanel.add(btnSearchBook);
        buttonPanel.add(btnBorrowBook);
        buttonPanel.add(btnReturnBook);
        buttonPanel.add(btnViewHistory);

        add(buttonPanel, BorderLayout.NORTH);

     
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

      
        btnAddBook.addActionListener(e -> addBook());
        btnSearchBook.addActionListener(e -> searchBook());
        btnBorrowBook.addActionListener(e -> borrowBook());
        btnReturnBook.addActionListener(e -> returnBook());
        btnViewHistory.addActionListener(e -> viewHistory());

        setVisible(true);
    }


    private void addBook() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField txtTitle = new JTextField();
        JTextField txtAuthor = new JTextField();

        panel.add(new JLabel("Book Title:"));
        panel.add(txtTitle);
        panel.add(new JLabel("Author:"));
        panel.add(txtAuthor);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = txtTitle.getText();
            String author = txtAuthor.getText();

            try {
                pst = con.prepareStatement("INSERT INTO books (title, author, availability) VALUES (?, ?, TRUE)");
                pst.setString(1, title);
                pst.setString(2, author);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book Added Successfully");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error Adding Book");
            }
        }
    }

   
    private void searchBook() {
        String title = JOptionPane.showInputDialog(this, "Enter Book Title to Search:");
        try {
            pst = con.prepareStatement("SELECT * FROM books WHERE title LIKE ?");
            pst.setString(1, "%" + title + "%");
            rs = pst.executeQuery();

            model.setRowCount(0); // Clear table
            model.setColumnIdentifiers(new Object[]{"Book ID", "Title", "Author", "Availability"});

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("availability") ? "Available" : "Not Available"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error Searching for Books");
        }
    }

    // Borrow a Book
    private void borrowBook() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField txtUserId = new JTextField();
        JTextField txtBookId = new JTextField();

        panel.add(new JLabel("User ID:"));
        panel.add(txtUserId);
        panel.add(new JLabel("Book ID:"));
        panel.add(txtBookId);

        int result = JOptionPane.showConfirmDialog(this, panel, "Borrow Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int userId = Integer.parseInt(txtUserId.getText());
            int bookId = Integer.parseInt(txtBookId.getText());

            try {
                pst = con.prepareStatement("SELECT availability FROM books WHERE book_id = ?");
                pst.setInt(1, bookId);
                rs = pst.executeQuery();

                if (rs.next() && rs.getBoolean("availability")) {
                    pst = con.prepareStatement("INSERT INTO borrow_history (book_id, user_id, borrow_date) VALUES (?, ?, CURDATE())");
                    pst.setInt(1, bookId);
                    pst.setInt(2, userId);
                    pst.executeUpdate();

                    pst = con.prepareStatement("UPDATE books SET availability = FALSE WHERE book_id = ?");
                    pst.setInt(1, bookId);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Book Borrowed Successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "Book Not Available");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error Borrowing Book");
            }
        }
    }

    // Return a Book
    private void returnBook() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField txtUserId = new JTextField();
        JTextField txtBookId = new JTextField();

        panel.add(new JLabel("User ID:"));
        panel.add(txtUserId);
        panel.add(new JLabel("Book ID:"));
        panel.add(txtBookId);

        int result = JOptionPane.showConfirmDialog(this, panel, "Return Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int userId = Integer.parseInt(txtUserId.getText());
            int bookId = Integer.parseInt(txtBookId.getText());

            try {
                pst = con.prepareStatement("UPDATE books SET availability = TRUE WHERE book_id = ?");
                pst.setInt(1, bookId);
                pst.executeUpdate();

                pst = con.prepareStatement("UPDATE borrow_history SET return_date = CURDATE() WHERE book_id = ? AND user_id = ?");
                pst.setInt(1, bookId);
                pst.setInt(2, userId);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book Returned Successfully");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error Returning Book");
            }
        }
    }

    // View Borrowing History
    private void viewHistory() {
        String userId = JOptionPane.showInputDialog(this, "Enter User ID to View History:");
        try {
            pst = con.prepareStatement(
                    "SELECT bh.borrow_id, b.title, b.author, bh.borrow_date, bh.return_date " +
                            "FROM borrow_history bh JOIN books b ON bh.book_id = b.book_id " +
                            "WHERE bh.user_id = ?");
            pst.setInt(1, Integer.parseInt(userId));
            rs = pst.executeQuery();

            model.setRowCount(0); // Clear table
            model.setColumnIdentifiers(new Object[]{"Borrow ID", "Title", "Author", "Borrow Date", "Return Date"});

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("borrow_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error Viewing History");
        }
    }

    public static void main(String[] args) {
        new LibraryManagementSystem1();
    }
}
