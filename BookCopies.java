public class BookCopies {

    private Book book;
    private int copies;

    public BookCopies(Book book, int copies) {
        this.book = book;
        this.copies = copies;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }
}
