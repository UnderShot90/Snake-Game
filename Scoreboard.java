public class Scoreboard {
    private SinglyLinkedList<GameEntry> board;
    private int capacity;

    public Scoreboard(int capacity) {
        this.capacity = capacity;
        board = new SinglyLinkedList<>();
    }

    public void add(GameEntry e) {
        SinglyLinkedList<GameEntry> temp = new SinglyLinkedList<>();
        boolean added = false;

        while (!board.isEmpty()) {
            GameEntry current = board.removeFirst();
            if (!added && e.getScore() > current.getScore()) {
                temp.addLast(e);
                added = true;
            }
            temp.addLast(current);
        }

        // If it wasn't added during the loop and there's still room, add it at the end
        if (!added && temp.size() < capacity) {
            temp.addLast(e);
        }

        // Restore elements to the main list
        while (!temp.isEmpty()) {
            board.addLast(temp.removeFirst());
        }

        // If we now have more than `capacity`, remove the *last* (lowest) score
        if (board.size() > capacity) {
            removeLast();
        }
    }

    private void removeLast() {
        SinglyLinkedList<GameEntry> temp = new SinglyLinkedList<>();
        for (int i = 0; i < board.size() - 1; i++) {
            temp.addLast(board.removeFirst());
        }
        board.removeFirst(); // remove the last (lowest score)
        while (!temp.isEmpty()) {
            board.addLast(temp.removeFirst());
        }
    }

    public GameEntry remove(int index) {
        if (index < 0 || index >= board.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        SinglyLinkedList<GameEntry> temp = new SinglyLinkedList<>();
        GameEntry removed = null;
        for (int i = 0; i < board.size(); i++) {
            GameEntry current = board.removeFirst();
            if (i == index) {
                removed = current;
            } else {
                temp.addLast(current);
            }
        }
        while (!temp.isEmpty()) {
            board.addLast(temp.removeFirst());
        }
        return removed;
    }

    public String toString() {
        return board.toString();
    }
}
