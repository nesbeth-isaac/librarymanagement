public class Book {

    private String bookName;
    private String bookCode;
    private String bookCopies;

    public Book(String bookName, String bookCode) {
        this.bookName = bookName;
        this.bookCode = bookCode;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public void setBookCopies(String bookCopies) {
        this.bookCopies = bookCopies;
    }

    public String getBookCopies() {
        return bookCopies;
    }
}
