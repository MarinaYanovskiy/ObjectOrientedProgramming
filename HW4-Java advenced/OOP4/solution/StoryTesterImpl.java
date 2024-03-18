package solution;

import provided.*;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class StoryTesterImpl implements StoryTester {
    private Object objectBackup;

    String firstFailedSentence;
    String expected;
    String result;
    int numFails;

    /** Creates and returns a new instance of testClass **/
    private static Object createTestInstance(Class<?> testClass) throws Exception {//this creates an instance of the received class (testClass: a Class instance).
       Object res;
        try { //we try to invoke the default c'tor
            Constructor<?> constructor = testClass.getConstructor();
            constructor.setAccessible(true);
            res=constructor.newInstance();
        } catch (Exception e) {
            //Inner classes case; Need to first create an instance of the enclosing class
            Class<?> enclosingClass = testClass.getEnclosingClass();
            // Recursively create an instance of the enclosing class
            Object enclosingInstance = createTestInstance(enclosingClass);
            // Find the constructor that takes the enclosing class instance as a parameter
            Constructor<?> constructor = testClass.getDeclaredConstructor(enclosingClass);
            // Create an instance of the nested class
            res= constructor.newInstance(enclosingInstance);
        }
        return res;
    }

    /** Returns true if c has a copy constructor, or false if it doesn't **/
    private boolean copyConstructorExists(Class<?> c){
        try {
            c.getDeclaredConstructor(c);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /** Assigns into objectBackup a backup of obj.
    /** See homework's pdf for more details on backing up and restoring **/
    private void backUpInstance(Object obj) throws Exception {
        Object res = createTestInstance(obj.getClass()); //create a new instance from the obj. type
        Field[] fieldsArr = obj.getClass().getDeclaredFields();// get all the fields declared by the object
        for(Field field : fieldsArr){
            field.setAccessible(true);
            Object fieldObject = field.get(obj); //get the value in the corresponding field in the given object
            if (fieldObject == null) {
                field.set(res, null); //set that field on a specific object to null: this field wasn't initiated
                continue; //we will continue to the next field
            }
            Class<?> fieldClass = fieldObject.getClass();  //create a meta class of the field- need to know how to save it

            if(fieldObject instanceof Cloneable){
                //Case1 - Object in field is cloneable. need to invoke the clone method!
                Method clone_m;
                try{
                    clone_m=fieldClass.getDeclaredMethod("clone");
                } catch (NoSuchMethodException e){
                    //means- understands clone but wasn't declared in that class. CHECK PIAZZA FOR THIS CASE!
                    clone_m=fieldClass.getMethod("clone");
                }
                clone_m.setAccessible(true);
                field.set(res,clone_m.invoke(field));
            }
            else if(copyConstructorExists(fieldClass)){
                // Case2 - Object in field is not cloneable but copy constructor exists
                Constructor<?> copyCtor = fieldClass.getConstructor(fieldClass);
                field.set(res,copyCtor.newInstance(field));
            }
            else{
                //Case3 - Object in field is not cloneable and copy constructor does not exist
                field.set(res,field);
            }
        }
        this.objectBackup = res;
    }

    /** Assigns into obj's fields the values in objectBackup fields.
    /** See homework's pdf for more details on backing up and restoring **/
    private void restoreInstance(Object obj) throws Exception{
        Field[] classFields = obj.getClass().getDeclaredFields();
        for(Field field : classFields) {
            field.setAccessible(true);
           field.set(obj,field.get(objectBackup));
        }
    }

    /** Returns the matching annotation class according to annotationName (Given, When or Then) **/
    private static Class<? extends Annotation> GetAnnotationClass(String annotationName){
        switch (annotationName) {
            case "Given":
                return Given.class;
            case "When":
                return When.class;
            default:
                return Then.class;
        }
    }

    @Override
    public void testOnInheritanceTree(String story, Class<?> testClass) throws Exception {
        if((story == null) || testClass == null) throw new IllegalArgumentException();

        this.numFails = 0;
        Object testInstance = createTestInstance(testClass);

        for(String sentence : story.split("\n")) {
            boolean methodFound = false;
            String[] words = sentence.split(" ", 2);

            String annotationName = words[0];
            Class<? extends Annotation> annotationClass = GetAnnotationClass(annotationName);

            String sentenceSub = words[1].substring(0, words[1].lastIndexOf(' ')); // Sentence without the parameter and annotation
            String parameter = sentence.substring(sentence.lastIndexOf(' ') + 1);
            
            // TODO: Complete.
        }

        // TODO: Throw StoryTestExceptionImpl if the story failed.
    }


    @Override
    public void testOnNestedClasses(String story, Class<?> testClass) throws Exception {
        //step1: check if the args are valid.
        if((story == null) || testClass == null) throw new IllegalArgumentException();

        //step2: get the first Given sentence.
        String[] givenAndAllTheRest = story.split(" ", 2);
        String aGivenSentence = givenAndAllTheRest[0].substring(givenAndAllTheRest[0].indexOf(' '), givenAndAllTheRest[0].lastIndexOf(' ')); // Sentence without the parameter and annotation

        //step3: create instance of test class
        this.numFails = 0;
        Object testInstance = createTestInstance(testClass); /**why do we need it?**/
        Object testedObject = null; //TODO: change.

        //TODO: complete step 4: find the matching class that understands the Given sentence.
        //TODO: complete step 5: create an instance of it, and of the object.

        //step 6: for every sentence in the rest of the story:
        for(String sentence : givenAndAllTheRest[1].split("\n")) {
            //step 6.1: break down the sentence to pieces.
            boolean methodFound = false;
            String[] words = sentence.split(" ", 2);


            String annotationName = words[0];
            Class<? extends Annotation> annotationClass = GetAnnotationClass(annotationName);

            String sentenceSub = words[1].substring(0, words[1].lastIndexOf(' ')); // Sentence without the parameter and annotation
            String parameter = sentence.substring(sentence.lastIndexOf(' ') + 1);

            //TODO: step 6.2: find and get a function with the proper annotation+sentence

            //step 6.3: if the annotation is "when"- backup the object.
            if(annotationName.equals("When"))
            {
             this.backUpInstance(testedObject);
            }
            try{
                //TODO: step 6.4: invoke the found function.
            } catch (Exception e) { //we will reach this if a "then" sentence failed.
                //step 6.5: restore the object.
                this.restoreInstance(testedObject);
                //TODO: step 6.6:: update faliure statistics.
            }
        }

        // TODO: step 7: Throw StoryTestExceptionImpl if the story failed.
    }

}


