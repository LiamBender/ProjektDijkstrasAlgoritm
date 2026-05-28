package model;

/**
 * Represents a server hall with an id, name and position.
 */
public class ServerHall {

    private final String id;
    private final String name;
    private final Position position;

    /**
     * Creates a server hall.
     *
     * @param id the unique id of the server hall
     * @param name the display name of the server hall
     * @param position the geographical position of the server hall
     */
    public ServerHall(final String id,
                      final String name,
                      final Position position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    /**
     * Gets the id.
     *
     * @return the server hall id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return the server hall name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the position.
     *
     * @return the server hall position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the display name of the server hall.
     *
     * @return the server hall name
     */
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Compares server halls using their unique id.
     *
     * @param obj the object to compare with
     * @return true if the ids are equal
     */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ServerHall)) {
            return false;
        }

        ServerHall other =
                (ServerHall) obj;

        return id.equals(other.id);
    }

    /**
     * Generates a hash code based on the id.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}