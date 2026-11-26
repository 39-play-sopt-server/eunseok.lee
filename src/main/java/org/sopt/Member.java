
package org.sopt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // 기존 4차 과제에서 사용하던 회원 이름
    private String name;

    protected Member() {}

    public Member(String email, String name, String password) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("회원 이름을 입력하세요.");
        }

        if (name.length() > 255) {
            throw new IllegalArgumentException("회원 이름은 255자를 넘을 수 없습니다.");
        }
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }
}
