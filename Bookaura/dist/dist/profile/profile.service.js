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
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.ProfileService = void 0;
const common_1 = require("@nestjs/common");
const mongoose_1 = require("@nestjs/mongoose");
const mongoose_2 = require("mongoose");
const profile_schema_1 = require("../schemas/profile.schema");
const songs_service_1 = require("../song/songs.service");
let ProfileService = class ProfileService {
    constructor(profileModel, songService) {
        this.profileModel = profileModel;
        this.songService = songService;
    }
    async create(data) {
        try {
            const existingProfile = await this.profileModel.findOne({ email: data.email });
            if (existingProfile) {
                throw new common_1.BadRequestException('Artist already has a profile.');
            }
            const newArtistProfile = new this.profileModel({
                artist: data.artist,
                email: data.email,
                bio: data.bio,
                genre: data.genre,
                description: data.description,
                imageData: data.imageData,
                imageContentType: data.imageContentType,
                uploadDate: new Date(),
            });
            return await newArtistProfile.save();
        }
        catch (error) {
            throw new common_1.BadRequestException(`Failed to save profile: ${error.message}`);
        }
    }
    async findByArtist(artist) {
        return this.profileModel
            .findOne({ email: artist })
            .exec();
    }
    async updateProfileByEmail(email, data) {
        try {
            const existingProfile = await this.profileModel.findOne({ email });
            if (!existingProfile) {
                throw new common_1.NotFoundException('Profile not found.');
            }
            existingProfile.artist = data.artist;
            existingProfile.bio = data.bio;
            existingProfile.genre = data.genre;
            existingProfile.description = data.description;
            existingProfile.imageData = data.imageData;
            existingProfile.imageContentType = data.imageContentType;
            existingProfile.uploadDate = new Date();
            return await existingProfile.save();
        }
        catch (error) {
            throw new common_1.BadRequestException(`Failed to update profile: ${error.message}`);
        }
    }
    async findProfileWithSongs(artist) {
        const profile = await this.profileModel.findOne({ artist }).exec();
        if (!profile) {
            throw new common_1.NotFoundException('Profile not found for this artist');
        }
        const songs = await this.songService.findByArtist(artist);
        return {
            profile,
            songs,
        };
    }
};
exports.ProfileService = ProfileService;
exports.ProfileService = ProfileService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, mongoose_1.InjectModel)(profile_schema_1.Profile.name)),
    __metadata("design:paramtypes", [mongoose_2.Model,
        songs_service_1.SongsService])
], ProfileService);
//# sourceMappingURL=profile.service.js.map