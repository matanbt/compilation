package TYPES;

public class TYPE_ARRAY_INSTANCE extends TYPE{
    public TYPE_ARRAY arrayType;

    public TYPE_ARRAY_INSTANCE (TYPE_ARRAY arrayType) {
        this.name = arrayType.name;
        this.arrayType = arrayType;
    }
}
