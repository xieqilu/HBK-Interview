/**
What are smart pointers? 
The answer is fairly simple; a smart pointer is a pointer which is smart. 
What does that mean? Actually, smart pointers are objects which behave like pointers but do more than a pointer. 
These objects are flexible as pointers and have the advantage of being an object 
(like constructor and destructors called automatically). 
A smart pointer is designed to handle the problems caused by using normal pointers (hence called smart).
*/
//Example about why we need smart pointer:

class Person  //person class
{
    int age;
    char* pName;

    public:
        Person(): pName(0),age(0) //constructor
        {
        }
        Person(char* pName, int age): pName(pName), age(age)  //constructor
        {
        }
        ~Person()  //destructor
        {
        }

        void Display()
        {
            printf("Name = %s Age = %d \n", pName, age);
        }
        void Shout()
        {
            printf("Ooooooooooooooooo",);
        } 
};

//client code to use person class
void main()
{
    Person* pPerson  = new Person("Scott", 25);
    pPerson->Display();
    delete pPerson; //every time we need to delete the pointer pPerson
}
/**Now look at this code, every time I create a pointer, 
I need to take care of deleting it. This is exactly what I want to avoid. 
I need some automatic mechanism which deletes the pointer. 
One thing which strikes to me is a destructor. But pointers do not have destructors, so what? 
Our smart pointer can have one. 
So we will create a class called SP which can hold a pointer to the Person class 
and will delete the pointer when its destructor is called.
*/
/**
Note the following things:

We have created an object of class SP which holds our Person class pointer. 
Since the destructor of the SP class will be called when this object goes out of scope, 
it will delete the Person class pointer (as its main responsibility); 
hence we don’t have the pain of deleting the pointer.

One more thing of major importance is that we should be able to call the Display method 
using the SP class object the way we used to call using the Person class pointer, 
i.e., the class should behave exactly like a pointer.

Since the smart pointer should behave like a pointer, 
it should support the same interface as pointers do; 
i.e., they should support the following operations.

Dereferencing (operator *)
Indirection (operator ->)

So we need to override the above two operator to make the sp class like a real pointer
*/

//a smart pointer class only support class person
//The main responsibility of this class is to 
//hold a pointer to the Person class and then delete it when its destructor is called.
//It should also support the interface of the pointer
class SP
{
private:
    Person*    pData; // pointer to person class
public:
    SP(Person* pValue) : pData(pValue)
    {
    }
    ~SP()
    {
        // delete pData in the destructor
        delete pData;
    }

    Person& operator* () //override operator* (dereference)
    {
        return *pData; //return the object pData point to
    }

    Person* operator-> () //override operator->
    {    
        return pData; //return the pointer pData itself
    }
};

//Generic Smart Pointer class that supports all types
//use Template
template < typename T > class SP
{
    private:
    T*    pData; // Generic pointer to be stored
    public:
    SP(T* pValue) : pData(pValue)
    {
    }
    ~SP()
    {
        delete pData;
    }

    T& operator* ()
    {
        return *pData;
    }

    T* operator-> ()
    {
        return pData;
    }
};

//client code to use generic smart pointer class
void main()
{
    SP<PERSON> p(new Person("Scott", 25)); //need to specify type T as PERSON
    p->Display();
    // Dont need to delete Person pointer..
}

//Now the smart pointer class can support all types,
//but still have problem
void main()
{
    SP<PERSON> p(new Person("Scott", 25));
    p->Display();
    {
        SP<PERSON> q = p;
        q->Display();
        // Destructor of Q will be called here..
    }
    p->Display();
}
/**
Look what happens here. p and q are referring to the same Person class pointer. 
Now when q goes out of scope, the destructor of q will be called which deletes the Person class pointer. 
Now we cannot call p->Display(); since p will be left with a dangling pointer and this call will fail. 
(Note that this problem would have existed even if we were using normal pointers instead of smart pointers.) 
We should not delete the Person class pointer unless no body is using it. How do we do that? 
Implementing a reference counting mechanism in our smart pointer class will solve this problem.
*/

//Reference counting class
/**
What we are going to do is we will have a reference counting class RC. 
This class will maintain an integer value which represents the reference count. 
We will have methods to increment and decrement the reference count
*/
class RC
{
    private:
    int count; // Reference count

    public:
    void AddRef()
    {
        // Increment the reference count
        count++;
    }

    int Release()
    {
        // Decrement the reference count and
        // return the reference count.
        return --count;
    }
};

/**
Now that we have a reference counting class, we will introduce this to our smart pointer class. 
We will maintain a pointer to class RC in our SP class and this pointer will be shared for 
all instances of the smart pointer which refers to the same pointer. For this to happen, 
we need to have an assignment operator and copy constructor in our SP class.
*/
template < typename T > class SP
{
private:
    T*    pData;       // pointer
    RC* reference; // Reference count, hold a pointer to RC, so can shared RC through all pointer objects

public:
    SP() : pData(0), reference(0)  //default constructor
    {
        // Create a new reference 
        reference = new RC();
        // Increment the reference count
        reference->AddRef();
    }

    SP(T* pValue) : pData(pValue), reference(0) //constructor with specified T
    {
        // Create a new reference 
        reference = new RC();
        // Increment the reference count
        reference->AddRef();
    }

    //Copy constructor
    SP(const SP<T>& sp) : pData(sp.pData), reference(sp.reference)
    {
        // Copy constructor
        // Copy the data and reference pointer
        // and increment the reference count
        reference->AddRef();
    }

    ~SP()  //Destructor
    {
        // Destructor
        // Decrement the reference count
        // if reference become zero delete the data
        if(reference->Release() == 0)
        {
            delete pData;
            delete reference;
        }
    }

    T& operator* ()
    {
        return *pData;
    }

    T* operator-> ()
    {
        return pData;
    }
    
    SP<T>& operator = (const SP<T>& sp) //override operator =, copy assignment action (r=p)
    {
        // Assignment operator
        if (this != &sp) // Avoid self assignment
        {
            // Decrement the old reference count
            // if reference become zero delete the old data
            if(reference->Release() == 0)
            {
                delete pData;
                delete reference;
            }

            // Copy the data and reference pointer
            // and increment the reference count
            pData = sp.pData;
            reference = sp.reference;
            reference->AddRef();
        }
        return *this;
    }
};

//Client code to use smart pointer with reference counting
void main()
{
    SP<PERSON> p(new Person("Scott", 25)); //constructor called
    p->Display();
    {
        SP<PERSON> q = p; //copy constructor called
        q->Display();
        // Destructor of q will be called here..

        SP<PERSON> r; //default constructor called
        r = p;  //copy assignment called here
        r->Display();
        // Destructor of r will be called here..
    }
    p->Display();
    // Destructor of p will be called here 
    // and person pointer will be deleted
}
/**
When we create a smart pointer p of type Person, the constructor of SP will be called, 
the data will be stored, and a new RC pointer will be created. 
The AddRef method of RC is called to increment the reference count to 1. 
Now SP q = p; will create a new smart pointer q using the copy constructor. 
Here the data will be copied and the reference will again be incremented to 2.
Now r = p; will call the assignment operator to assign the value of p to q. 
Here also we copy the data and increment the reference count, thus making the count 3. 
When r and q go out of scope, the destructors of the respective objects will be called. 
Here the reference count will be decremented, but data will not be deleted unless the reference count becomes zero. 
This happens only when the destructor of p is called. Hence our data will be deleted only when no body is referring to it.
*/

