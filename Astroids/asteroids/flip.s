    PRESERVE8 ; Indicate the code here preserve 
              ; 8 byte stack alignment
    THUMB     ; Indicate THUMB code is used
    AREA    |.text|, CODE, READONLY   ; Start of CODE area
    EXPORT flip
    ENTRY
flip     FUNCTION
    ; expects arg in R0; returns val in R0
    PUSH {R1,R2, LR} ; save working registers and LR
    RBIT R0 
	MOV R0, R0 LSR #24
return
    POP {R1,R2, LR} ; restore saved registers
    BX LR
    ENDFUNC
    END            ; End of file
