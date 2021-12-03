package TYPES;

public class TYPE_NIL extends TYPE
{
    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static TYPE_NIL instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected TYPE_NIL() {}

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static TYPE_NIL getInstance()
    {
        if (instance == null)
        {
            instance = new TYPE_NIL();
        }
        return instance;
    }

    @Override
    public boolean canBeRtnType() {
        return false;
    }

    @Override
    public boolean canBeAssigned() {
        return false;
    }
}