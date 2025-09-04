package com.example.member.service;

import com.example.member.dto.MemberDTO;
import com.example.member.entity.MemberEntity;
import com.example.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        //1.dto -> entity 변환
        //2. repository save 메서드 호출
        //repository save 메서드 호출 (조건 : entity 객체로 넘겨줘야함)
        memberRepository.save(MemberEntity.toMemberEntity(memberDTO));
    }

    public MemberDTO login(MemberDTO memberDTO) {
       /*
       1.회원이 입력한 이메일로 DB에서 조회를 함
       2.DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
        */
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(
            memberDTO.getMemberEmail());
        if (byMemberEmail.isPresent()) {
            //조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            MemberEntity memberEntity = byMemberEmail.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                //비밀번호가 일치
                //entity -> dto 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            }else{
                //비밀번호 불일치 (로그인 실패)
                return null;
            }
        }else{
            //조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntities :memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntities));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> byId = memberRepository.findById(id);
        if(byId.isPresent()) {
            MemberDTO memberDTO = MemberDTO.toMemberDTO(byId.get());
            return memberDTO;
        }else {
            return null;
        }
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> byEmail = memberRepository.findByMemberEmail(myEmail);
        if(byEmail.isPresent()) {
            return MemberDTO.toMemberDTO(byEmail.get());
        }else{
            return null;
        }

    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
