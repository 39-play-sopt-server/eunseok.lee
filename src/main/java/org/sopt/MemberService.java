package org.sopt;

import org.springframework.stereotype.Service;
import org.sopt.exception.MemberNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public MemberService(MemberRepository memberRepository, PostRepository postRepository) {
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    public Member createMember(String name) {
        Member member = new Member(name);
        return memberRepository.save(member);
    }

    public void deleteMember(Long memberId) {
        if (memberId == null || memberId < 1) {
            throw new MemberNotFoundException("존재하지 않는 회원입니다.");
        }
        Member member = memberRepository.findByIdForUpdate(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        postRepository.deleteByMemberId(memberId);
        postRepository.flush();
        memberRepository.delete(member);
    }
}
