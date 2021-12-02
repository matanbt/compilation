package TYPES;

public class TYPE_ARRAY extends TYPE
{
    /* The type of every element in the array */
    public TYPE elementType;

    public TYPE_ARRAY(String name, TYPE elementType)
    {
        this.name = name;
        this.elementType = elementType;
    }
}