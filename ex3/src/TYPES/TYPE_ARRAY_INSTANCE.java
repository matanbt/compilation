package TYPES;

public class TYPE_ARRAY_INSTANCE extends TYPE implements I_INSTANCE_TYPE
{
    public TYPE_ARRAY arrayType;

    public TYPE_ARRAY_INSTANCE (TYPE_ARRAY arrayType) {
        this.name = arrayType.name;
        this.arrayType = arrayType;
    }

    public TYPE getElementType()
    {
        return this.arrayType.elementType;
    }

    public TYPE getSymbolType() {
        return this.arrayType;
    }
}
