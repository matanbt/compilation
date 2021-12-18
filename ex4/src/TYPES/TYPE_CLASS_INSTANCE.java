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
public class TYPE_CLASS_INSTANCE extends TYPE implements I_INSTANCE_TYPE{

    public TYPE_CLASS fromClass; // back pointer to the L-class creates instances from this TYPE
    public TYPE_CLASS_INSTANCE (TYPE_CLASS fromClass) {
        this.name = fromClass.name;
        this.fromClass = fromClass;
    }

    // note: a class can subclass itself
    public boolean isSubClassOf(TYPE_CLASS_INSTANCE potentialFather) {
        return false; // TODO on class task: IMPLEMENT
    }

    @Override
    public TYPE getSymbolType() {
        return this.fromClass;
    }

}
