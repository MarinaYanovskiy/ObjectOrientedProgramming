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
            constructor.setAccessible(true);
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

        //step3: create instance of test class--- no need!
        this.numFails = 0;
        Object testedObject = null; //TODO: change.

        // step 4: find the matching class that understands the Given sentence.
        Class<?> declaresGiven= findClassDeclaringGivenForNested(aGivenSentence,testClass);

        // step 5: create an instance of it, and TODO: of the object.
        Object testInstance=createTestInstance(declaresGiven);


        //step 6: for every sentence in the rest of the story:
        for(String sentence : givenAndAllTheRest[1].split("\n")) {
            //step 6.1: break down the sentence to pieces.
            boolean methodFound = false;
            String[] words = sentence.split(" ", 2);

            String annotationName = words[0];
            String sentenceSub = words[1].substring(0, words[1].lastIndexOf(' ')); // Sentence without the parameter and annotation
            String parameter = sentence.substring(sentence.lastIndexOf(' ') + 1);


            //step 6.2: find and get a function with the proper annotation+sentence
            Method methodToInvoke= findMatchingMethodInNested(declaresGiven,annotationName,sentenceSub);

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


    /** Find the class that declares the Given method.
     *
     * @param sentenceSub- the value that the annotation should have.
     * @param testClass- the class we search in
     * @return the class that declares the method by the rules in the PDF.
     */
    public static Class<?> findClassDeclaringGivenForNested(String sentenceSub,Class<?> testClass){
        if(findMatchingMethodInInheritance(testClass,"Given",sentenceSub)!=null){
            return testClass;
        }

        for (Class<?> aNestedClass : testClass.getDeclaredClasses()){
            if(findMatchingMethodInInheritance(aNestedClass,"Given",sentenceSub)!=null){
               return aNestedClass;
            }
            return findClassDeclaringGivenForNested(sentenceSub,aNestedClass);
        }
        return null;
    }

    /** Find a method from inheritance hierarchy
     *
     * @param testClass: a class to begin the search with.
     * @param strAnnotation: the annotation that should annotate the method.
     * @param sentenceSub: the value the annotation should have.
     * @return a method annotated by the annotation with the received value.
     */
    public static Method findMatchingMethodInInheritance(Class<?> testClass, String strAnnotation, String sentenceSub){
        for (Method aMethod: testClass.getDeclaredMethods()){ //check if the current class understands declares that method
            if(doesMethodHaveAnnotationWithValue(aMethod,strAnnotation,sentenceSub)){
                return aMethod;
            }
        }
        //if reached here - the current class does not declare that method. need to search in it inheritance hierarchy
        if (testClass !=Object.class){
           return findMatchingMethodInInheritance(testClass.getSuperclass(),strAnnotation,sentenceSub);
        }
       return null;
    }

    /** Will be filled later due to questions about the pdf.
     *
     * @param testClass
     * @param strAnnotation
     * @param sentenceSub
     * @return
     */
    //ToDo: check PIAZZA regarding nested classes!!!!!
    public static Method findMatchingMethodInNested(Class<?> testClass, String strAnnotation, String sentenceSub){
        Method method =findMatchingMethodInInheritance(testClass,strAnnotation,sentenceSub);
        if (method!=null){
            return method;
        }
        //if reached here - the current class did not find that method in its hierarchy. need to search in the enclosing class hierarchy.
        if(testClass.getEnclosingClass()!=null) {
           return findMatchingMethodInNested(testClass.getEnclosingClass(),strAnnotation,sentenceSub);
        }
        //if reached here - this is a top level class.
        return null;
    }

    /****
     * This function checks if a method is annotated by a specific annotation with a specific value.
     * @param aMethod: a method we will check.
     * @param strAnnotation: the annotation to search for.
     * @param sentenceSub: the value that annotation should have.
     * @return true if indeed the  method is annotated by the specific annotation with the specific value
     */
    public static boolean doesMethodHaveAnnotationWithValue(Method aMethod, String strAnnotation ,String sentenceSub){
       Class<? extends Annotation> annClass=GetAnnotationClass(strAnnotation);
       String annotationValue = null;
       switch (strAnnotation){
           case "Given":
                Given isGiven= (Given) aMethod.getAnnotation(annClass);
                annotationValue=isGiven.value();
                break;
           case "When":
               When isWhen= (When) aMethod.getAnnotation(annClass);
               annotationValue=isWhen.value();
               break;
           case "Then":
               Then isThen = (Then) aMethod.getAnnotation(annClass);
               annotationValue=isThen.value();
               break;
       }
       if(annotationValue!=null){
           if(annotationValue.substring(0,annotationValue.lastIndexOf(" ")).equals(sentenceSub))
           {
               return true;
           }
       }
       return false;
    }

}




