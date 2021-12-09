package TYPES;

public class TYPE_NIL_INSTANCE extends TYPE
{
    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static TYPE_NIL_INSTANCE instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected TYPE_NIL_INSTANCE() {}

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static TYPE_NIL_INSTANCE getInstance()
    {
        if (instance == null)
        {
            instance = new TYPE_NIL_INSTANCE();
        }
        return instance;
    }
}
