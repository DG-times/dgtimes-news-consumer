package com.dgtimes.consumer.service;

import com.dgtimes.consumer.repository.RelatedKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelatedKeywordService {

    private final RelatedKeywordRepository relatedKeywordRepository;

    public void searchValue(String keyword) {
        if (relatedKeywordRepository.findByValue(keyword) == null) {
            relatedKeywordRepository.queryByInsertValue(keyword);
        }
    }

    // 관계 여부 확인 - 없으면 관계 생성, 있으면 가중치 업데이트
    public void searchRelatedValue(String keyword, String relatedKeyword) {
        Integer setWeight = relatedKeywordRepository.findByRelatedValue(keyword, relatedKeyword);
        if(setWeight == null) {
            relatedKeywordRepository.queryByRelated(keyword,relatedKeyword,1);
        } else {
            // 등록되어있는 관계의 weight의 값을 업데이트하도록 -> 불러와서 사용
            setWeight += 1;
            Integer weight = relatedKeywordRepository.findByRelatedValue(keyword, relatedKeyword);
            try{
                relatedKeywordRepository.queryBySetRelatedValue(keyword,weight,relatedKeyword,setWeight);
            } catch (Exception e) {
                //System.out.println("NoSuchRecordException");
            }
        }
    }
}
