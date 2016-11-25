package com.wkswind.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用在{@link IPersonPrint#printPerson(Person)}中作为参数，实现在{@link PersonPrint}
 */
public class Person implements Parcelable {
    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", male=" + male +
                ", salary=" + salary +
                '}';
    }

    int age;
    String name;
    boolean male;
    double salary;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.age);
        dest.writeString(this.name);
        dest.writeByte(this.male ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.salary);
    }

    public Person() {
    }

    protected Person(Parcel in) {
        this.age = in.readInt();
        this.name = in.readString();
        this.male = in.readByte() != 0;
        this.salary = in.readDouble();
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
