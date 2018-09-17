package service;

abstract class Service {
    int convertId(String id) throws NumberFormatException{
        if (id.length() > 1 && id.startsWith("0")) {
            throw new NumberFormatException("ID shouldn't start with 0");
        }
        return Integer.parseInt(id);
    }
}
