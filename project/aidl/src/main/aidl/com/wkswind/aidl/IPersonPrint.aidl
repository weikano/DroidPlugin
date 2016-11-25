// IPersonPrint.aidl
package com.wkswind.aidl;
import com.wkswind.aidl.Person;
// Declare any non-default types here with import statements

interface IPersonPrint {

    void printPerson(in Person person);
}
