package com.tools.seoultech.timoproject.post.controller.validation.comment;

import com.tools.seoultech.timoproject.post.controller.validation.SearchingFilterCheck;
import com.tools.seoultech.timoproject.post.domain.dto.Comment_SearchingFilterDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.Objects;

public class CommentFilterValidator implements ConstraintValidator<SearchingFilterCheck, Comment_SearchingFilterDTO> {
    @Override
    public boolean isValid(Comment_SearchingFilterDTO filterDto, ConstraintValidatorContext context) {
        if(Objects.nonNull(filterDto.commentId()) && filterDto.commentId() <= 0) return false;
        if(Objects.nonNull(filterDto.postId()) && filterDto.postId() <= 0) return false;
        if(Objects.nonNull(filterDto.memberId()) && filterDto.memberId() <= 0) return false;
        if(!isExistFieldInComment(filterDto.sortBy())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid sortBy value: " + filterDto.sortBy())
                    .addConstraintViolation();
            return false;
        }
         // TODO: 이어서
        return true;
    }

    @Override
    public void initialize(SearchingFilterCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    private boolean isExistFieldInComment(String sortBy){
        Field[] fields = Comment.class.getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().equals(sortBy)) return true;
        }
        fields = Comment.class.getSuperclass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().equals(sortBy)) return true;
        }
        return false;
    }
}
