# Carbon8 ISA

## Registers

| Register | Bits | Purpose            | Naming |
|----------|------|--------------------|--------|
| 0x00     | 8    | General            | a      |
| 0x01     | 8    | General            | b      |
| 0x02     | 8    | General            | c      |
| 0x03     | 8    | General            | d      |
| 0x04     | 8    | Program Counter    | hpc    |
| 0x05     | 8    | Program Counter    | lpc    |
| 0x06     | 8    | Stack base address | sb     |
| 0x07     | 8    | Stack pointer      | sp     |

## Instructions
| Instruction | Parameters        | Description                                     | Naming |
|-------------|-------------------|-------------------------------------------------|-------|
| 0x00-0x02   | value             | Load value into register `a`                    | lda   |
| 0x03-0x05   | value             | Load value into register `b`                    | ldb   |
| 0x06-0x08   | value             | Load value into register `c`                    | ldc   |
| 0x09-0x0B   | value             | Load value into register `d`                    | ldd   |
| 0x0C-0x0E   | value             | Add value to register `a`                       | adda  |
| 0xF-0x11    | value             | Add value to register `b`                       | addb  |
| 0x12-0x14   | value             | Add value to register `c`                       | addc  |
| 0x15-0x17   | value             | Add value to register `d`                       | addd  |
| 0x18-0x1A   | value             | Subtract value to register `a`                  | suba  |
| 0x1B-0x1D   | value             | Subtract value to register `b`                  | subb  |
| 0x1E-0x20   | value             | Subtract value to register `c`                  | subc  |
| 0x21-0x23   | value             | Subtract value to register `d`                  | subd  |
| 0x24-0x26   | address, value    | Write value into memory at address              | wrt   |
| 0x27-0x29   | address, register | Read value from memory at address into register | rd    |
 | 0x2A-0x2C   | value             | Load value into program counter                 | lpc   |
| 0xFF        | N/A               | Program end                                     | N/A   |

## Memory
Up to 32K