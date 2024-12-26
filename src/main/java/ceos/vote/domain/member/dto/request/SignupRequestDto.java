package ceos.vote.domain.member.dto.request;

import ceos.vote.domain.member.entity.Member;
import ceos.vote.domain.member.entity.PartType;
import ceos.vote.domain.member.entity.TeamType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequestDto(

        @Schema(description = "이름", example = "홍길동")
        @NotNull(message = "이름은 필수 입력 사항입니다.")
        String name,

        @Schema(description = "아이디", example = "ceos2024")
        @NotNull(message = "아이디는 필수 입력 사항입니다.")
        String userId,

        @Schema(description = "비밀번호", example = "12345678")
        @NotNull(message = "비밀번호는 필수 입력 사항입니다.")
        String password,

        @Schema(description = "이메일", example = "ceos@gmail.com")
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        String email,

        @Schema(description = "소속 팀명", example = "포토그라운드 | 엔젤브릿지 | 페달지니 | 케이크웨이 | 커피딜")
        @NotNull(message = "소속 팀명은 필수 입력 사항입니다.")
        String team,

        @Schema(description = "소속 파트", example = "프론트엔드 | 백엔드")
        @NotNull(message = "소속 파트는 필수 입력 사항입니다.")
        String part
) {
    public Member toEntity(String encodedPassword, PartType part, TeamType team) {
        return Member.builder()
                .userId(userId)
                .password(encodedPassword)
                .email(email)
                .role("ROLE_USER")
                .part(part)
                .name(name)
                .team(team)
                .voteBack(false)
                .voteFront(false)
                .voteTeam(false)
                .build();
    }
}
