package model;

import java.util.Date;
import java.util.Objects;

public abstract class People {
    private Long id;
    private String lastName, firstName, patronymic;
    private Date birthday;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        People people = (People) o;
        return id == people.id && Objects.equals(lastName, people.lastName) && Objects.equals(firstName, people.firstName) && Objects.equals(patronymic, people.patronymic) && Objects.equals(birthday, people.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, firstName, patronymic, birthday);
    }

    @Override
    public String toString() {
        return "People{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
