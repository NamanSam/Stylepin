package com.stylepin.controller;
import com.stylepin.dto.*;
import com.stylepin.service.SavedOutfitService;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
@RestController @Validated @RequestMapping("/api/users/me/saved-outfits")
public class SavedOutfitController {
    private final SavedOutfitService saves;
    public SavedOutfitController(SavedOutfitService saves){this.saves=saves;}
    @GetMapping public PageResponseDTO<OutfitResponseDTO> list(@RequestParam(defaultValue="0") @Min(0) int page,
        @RequestParam(defaultValue="24") @Min(1) @Max(100) int size){return saves.list(page,size);}
    @PostMapping("/{outfitId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@PathVariable @Positive Long outfitId){saves.save(outfitId);}
    @DeleteMapping("/{outfitId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable @Positive Long outfitId){saves.remove(outfitId);}
}
