// Defines the package in which this class is located within the project's structure.
package com.nighthawk.spring_portfolio.mvc.auth;

// These imports are for JPA annotations that define the class's relationship with the database and its structure.
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Lombok annotations to reduce boilerplate code for data objects.
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// '@Data' - A Lombok annotation to create all the getters, setters, equals, hash, and toString methods, based on the fields.
@Data
// '@NoArgsConstructor' - A Lombok annotation to generate a constructor with no parameters.
@NoArgsConstructor
// '@AllArgsConstructor' - A Lombok annotation to generate a constructor with all parameters for the fields.
@AllArgsConstructor
// '@Entity' - A JPA annotation to make this class ready for storage in a JPA-based data store (like a relational database).
@Entity
public class PersonRole {
    // '@Id' - This JPA annotation marks the field below as the primary key for the database table corresponding to this entity.
    @Id
    // '@GeneratedValue' - This annotation is used to specify that the value will be generated automatically (in this case, using the AUTO strategy).
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; // The unique ID for the entity (primary key).

    // '@Column' - This annotation specifies the properties for the database column to which the field will be mapped.
    // 'unique = true' - This element of the @Column annotation makes sure that the column is unique, preventing duplicate values.
    @Column(unique = true)
    private String name; // Represents the unique role name.
}
