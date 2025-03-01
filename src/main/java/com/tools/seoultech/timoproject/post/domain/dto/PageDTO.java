package com.tools.seoultech.timoproject.post.domain.dto;

import lombok.Builder;
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
    public record Request(
            int page,
            int size
    ) {
        public Request() {
            this(1, 10);
        }
        public Pageable getPageable(Sort sort){
            return PageRequest.of(page-1, size, sort);
        }
        public static Request of(int page, int size){
            return new Request(page, size);
        }

    }
    @Builder
    public record Response<DTO, EN>(
            int totalPage,
            int totalSize,
            int cur_page,
            int start,
            int end,
            boolean prev,
            boolean next,
            List<DTO> dtoList,
            List<Integer> pageList
    ) {
        private Response(Page<EN> result, Function<EN, DTO> fn) {
            this(
                    result.getTotalPages(), // totalPage
                    result.getSize(),       // totalSize
                    result.getPageable().getPageNumber() + 1, // cur_page
                    calculateStart(result), // start
                    calculateEnd(result),   // end
                    calculatePrev(result),  // prev
                    calculateNext(result),  // next
                    mapToDTO(result, fn),   // dtoList
                    calculatePageList(result) // pageList
            );
        }

        public static <EN, DTO> Response of(Page<EN> result, Function<EN, DTO> fn){
            return new Response(result, fn);
        }

        private static <EN> int calculateStart(Page<EN> result) {
            int curPage = result.getPageable().getPageNumber() + 1;
            int tmpEnd = (int) (Math.ceil(curPage / 10.0)) * 10;
            return tmpEnd - 9;
        }

        private static <EN> int calculateEnd(Page<EN> result) {
            int curPage = result.getPageable().getPageNumber() + 1;
            int tmpEnd = (int) (Math.ceil(curPage / 10.0)) * 10;
            int totalPage = result.getTotalPages();
            return (totalPage > tmpEnd) ? tmpEnd : totalPage;
        }

        private static <EN> boolean calculatePrev(Page<EN> result) {
            int start = calculateStart(result);
            return start > 1;
        }

        private static <EN> boolean calculateNext(Page<EN> result) {
            int totalPage = result.getTotalPages();
            int tmpEnd = (int) (Math.ceil((result.getPageable().getPageNumber() + 1) / 10.0)) * 10;
            return totalPage > tmpEnd;
        }

        private static <EN, DTO> List<DTO> mapToDTO(Page<EN> result, Function<EN, DTO> fn) {
            return result.stream()
                    .map(fn)
                    .collect(Collectors.toList());
        }

        private static <EN> List<Integer> calculatePageList(Page<EN> result) {
            int start = calculateStart(result);
            int end = calculateEnd(result);
            return IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
        }
    }
}
