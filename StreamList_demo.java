import com.demo.model.Account;

import java.util.*;
import java.util.stream.*;

public class StreamList_demo {
    static class Person{
        String name;
        int age;
        String city;
        int salary;
        Person(String name, int age, String city, int salary) {
            this.name = name;
            this.age = age;
            this.city = city;
            this.salary = salary;
        }
        public String toString(){
            return name + "(" + age + ")岁，"+city + "," + salary+"元";
        }
        public static void main(String[] args){
            List<Person> people = Arrays.asList(
                    new Person("张三", 25, "北京", 8000),
                    new Person("李四", 30, "上海", 12000),
                    new Person("王五", 22, "北京", 6000),
                    new Person("赵六", 35, "上海", 20000),
                    new Person("孙七", 28, "广州", 10000)
            );
            List<Person> adults = people.stream()
                    .filter(p -> p.age >=25)
                    .collect(Collectors.toList());
            System.out.println("1. 年龄>=25: " + adults);
            List<String> names = people.stream()
                    .map(p -> p.name)
                    .collect(Collectors.toList());
            System.out.println("2. 姓名: " + names);
            List<Person> bySalary = people.stream()
                    .sorted(Comparator.comparingInt((Person p) -> p.salary).reversed())
                    .collect(Collectors.toList());
            System.out.println("3. 工资降序: " + bySalary);

        }

}}
