package resource;


public abstract class Resource {
    protected int convertId(String id) throws NumberFormatException{
        int entityId;
        if (id.length() > 1 && id.startsWith("0")) {
            throw new NumberFormatException("ID shouldn't start with 0");
        }
        return Integer.parseInt(id);
    }

}
