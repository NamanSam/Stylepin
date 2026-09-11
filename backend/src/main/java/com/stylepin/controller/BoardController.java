package com.stylepin.controller;
import com.stylepin.dto.*;
import com.stylepin.service.BoardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
@RestController @Validated @RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boards;
    public BoardController(BoardService boards){this.boards=boards;}
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public BoardResponseDTO create(@Valid @RequestBody BoardRequestDTO request){return boards.create(request);}
    @GetMapping public PageResponseDTO<BoardResponseDTO> list(@RequestParam(defaultValue="0") @Min(0) int page,
        @RequestParam(defaultValue="24") @Min(1) @Max(100) int size){return boards.list(page,size);}
    @GetMapping("/{id}") public BoardDetailResponseDTO detail(@PathVariable @Positive Long id,
        @RequestParam(defaultValue="0") @Min(0) int page,@RequestParam(defaultValue="24") @Min(1) @Max(100) int size){return boards.detail(id,page,size);}
    @PostMapping("/{id}/outfits/{outfitId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@PathVariable @Positive Long id,@PathVariable @Positive Long outfitId){boards.add(id,outfitId);}
    @DeleteMapping("/{id}/outfits/{outfitId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable @Positive Long id,@PathVariable @Positive Long outfitId){boards.remove(id,outfitId);}
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id){boards.delete(id);}
}
