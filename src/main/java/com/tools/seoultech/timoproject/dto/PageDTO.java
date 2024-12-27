package com.tools.seoultech.timoproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class Request {
        private int page;
        private int size;

        public Request() {
            this.page = 1;
            this.size = 10;
        }
        public Pageable getPageable(Sort sort){
            return PageRequest.of(page-1, size, sort);
        }
        public static Request of(int page, int size){
            return new Request(page, size);
        }
    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class Response<DTO, EN> {
        private int totalPage, totalSize, cur_page;
        private int start, end;
        private boolean prev, next;
        private List<DTO> dtoList;
        private List<Integer> pageList;

        public Response(Page<EN> result, Function<EN, DTO> fn) {
            dtoList = result.stream()
                    .map(fn)
                    .collect(Collectors.toList());
            totalPage = result.getTotalPages();
            makePageList(result.getPageable());
        }
        private void makePageList(Pageable pageable) {
            this.cur_page = pageable.getPageNumber() + 1;
            this.totalSize = pageable.getPageSize();

            int tmpEnd = (int)(Math.ceil(this.cur_page/10.0))*10;
            start = tmpEnd - 9;
            end = (totalPage > tmpEnd) ? tmpEnd : totalPage;
            prev = (start > 1);
            next = (totalPage > tmpEnd);

            pageList = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
        }
        public static <EN, DTO> Response of(Page<EN> result, Function<EN, DTO> fn){
            return new Response(result, fn);
        }
    }
}
