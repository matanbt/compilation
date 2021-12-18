package TYPES;

public class TYPE_ARRAY extends TYPE implements I_SYMBOL_TYPE
{
    /* The type of every element in the array */
    public TYPE elementType;
    public TYPE_ARRAY_INSTANCE arrayInstanceType;

    public TYPE_ARRAY(String name, TYPE elementType)
    {
        this.name = name;
        this.elementType = elementType;
        this.arrayInstanceType = new TYPE_ARRAY_INSTANCE(this);
    }

    public TYPE getInstanceType() {
        return this.arrayInstanceType;
    }
}