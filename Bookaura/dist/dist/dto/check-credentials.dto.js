"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.CheckCredentialsDto = void 0;
const swagger_1 = require("@nestjs/swagger");
class CheckCredentialsDto {
}
exports.CheckCredentialsDto = CheckCredentialsDto;
__decorate([
    (0, swagger_1.ApiProperty)({ example: 'student123', description: 'The unique ID of the student' }),
    __metadata("design:type", String)
], CheckCredentialsDto.prototype, "studentId", void 0);
__decorate([
    (0, swagger_1.ApiProperty)({ example: 'schoolpassword', description: 'The password for the student' }),
    __metadata("design:type", String)
], CheckCredentialsDto.prototype, "studentPassword", void 0);
//# sourceMappingURL=check-credentials.dto.js.map