package com.tools.seoultech.timoproject.post.controller.validation;

import com.tools.seoultech.timoproject.post.domain.dto.SearchingFilterDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class SearchingFilterValidator implements ConstraintValidator<SearchingFilterCheck, SearchingFilterDTO> {
    private String[] checkList = {};

    @Override
    public boolean isValid(SearchingFilterDTO requestDto, ConstraintValidatorContext context) {

        // 멤버 ID 위배 조건
        if(requestDto.memberId() != null && requestDto.memberId() <= 0)
            return false;

        // 정렬 기준 위배 조건
        if(!isExistFieldInPost(requestDto.sortBy())){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid sortBy value: " + requestDto.sortBy())
                    .addConstraintViolation();
            return false;
        }
        // 정렬 옵션 null 필드 체크
        if(requestDto.sortOrder() == null || requestDto.sortBy() == null) {
            System.err.println("dfdfdfdfdfdf");
            return false;
        }

        return true;
    }

    @Override
    public void initialize(SearchingFilterCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    private boolean isExistFieldInPost(String requestField){
        // Post 엔티티 필드에서 매칭 검사
        for(Field field : Post.class.getDeclaredFields()) {
            if(field.getName().equals(requestField))
                return true;
        }
        // Post 엔티티 부모 필드에서 매칭 검사
        for(Field field : Post.class.getSuperclass().getDeclaredFields()) {
            System.err.println(field.getName());
            if(field.getName().equals(requestField))
                return true;
        }
        return false;
    }
}
