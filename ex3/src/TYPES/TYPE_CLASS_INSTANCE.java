package TYPES;
/*
    USAGE:
    Instance of this class will be the REAL type that each L-class-instance will have.
    This is an important distinguishing between L-classes and L-classes-instances.
 */

/*
    EXPLANATION:
    consider the following case:
    `
    class A {}
    void main() { A obj := new A; B b }
    `
    When we're in main we'll have 2 symbols in the table: ('A', TYPE_CLASS-java-instance with name='A')
    and ('obj', TYPE_CLASS_INSTANCE-java-instance belongs uniquely to TYPE_CLASS-java-instance with name='A').
    We do this bc we HAVE to distinguish between the type of A (not assignable, a class in L).
    and the type of 'obj' (an assignable instance of A in L).

    Consider the following code as well:
    `
    class A {}
    void main() { A a_1 := new A; A a_2 := new A; }
    `
    Even though a_1 is another variable than a_2, it holds that both have the *same java-instance* of TYPE_CLASS_INSTANCE.
 */
public class TYPE_CLASS_INSTANCE extends TYPE {

    public TYPE_CLASS wasCreatedFromClass; // back pointer to the L-class creates instances from this TYPE
    public TYPE_CLASS_INSTANCE (String name, TYPE_CLASS wasCreatedFromClass) {
        this.name = name;
        this.wasCreatedFromClass = wasCreatedFromClass;
    }

    public boolean isClass () {
        return false; // THIS IS NOT L-CLASS, but the type of instance of one
    }


    // Stating the defaults of the following explicitly:

    public boolean canBeAssigned() {
        return true;
    }

    public boolean canBeAssignedNil() {
        return true;
    }

    public boolean canBeRtnType() {
        return true;
    }
}
