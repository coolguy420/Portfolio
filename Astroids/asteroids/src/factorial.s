    PRESERVE8 ; Indicate the code here preserve 
              ; 8 byte stack alignment
    THUMB     ; Indicate THUMB code is used
    AREA    |.text|, CODE, READONLY   ; Start of CODE area
    EXPORT fact
    ENTRY
fact     FUNCTION
    ; expects arg in R0; returns val in R0
    PUSH {R1,R2, LR} ; save working registers and LR
	MOV R1, R0
    RBIT R2, R1
	LSR R0, R2, #24
    POP {R1,R2, LR} ; restore saved registers
    BX LR
    ENDFUNC
    END            ; End of file
