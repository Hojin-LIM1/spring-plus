package org.example.expert.config;
import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;  // 권한 설정을 위해 필요하다구~
import org.springframework.security.core.authority.SimpleGrantedAuthority; // 권한 설정을 위해 필요하다구~
import org.springframework.security.core.userdetails.UserDetails; //user를 객체에 담아담아~

import java.util.Collection;
import java.util.List;


//음 일단 스프링 시큐리티에서 제공하는 UserDetails를 ,contextHolder에 담아야 혀
//스프링 시큐리티에서 제거하는 UserDetails를 상속받아서 Implements 클래스를 하나 만들자
// 아이디랑, 이메일이랑, 권한이랑, lv2 연계해서 닉네임이랑
@Getter
public class UserDetailsImpl implements UserDetails {

    private final Long userId;
    private final String email;
    private final UserRole userRole;
    private final String nickname;

    public UserDetailsImpl(Long userId, String email, UserRole userRole, String nickname) {
        this.userId = userId;
        this.email = email;
        this.userRole = userRole;
        this.nickname = nickname;
    }

    // 권한을 어떤 형태로든 담아도 됨 -> Collection으로
    // 근데 springSecurity 규격을 맞차야되서 ROLE은 붙여야되~ 불변 리스트로 만들어버리기~
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    // 패스워드 넣고
    @Override
    public String getPassword() { return null; }

    // 이름 넣고
    @Override
    public String getUsername() { return email; }

    // 계정 상태도 췌크 췌크~ 만료됨? 잠김? 사용 가능함?
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
