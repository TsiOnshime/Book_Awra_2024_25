import { ProfileService } from './profile.service';
import { CreateProfileDto } from '../dto/create-profile.dto';
import { Profile } from "../schemas/profile.schema";
export declare class ProfileController {
    private readonly profileService;
    constructor(profileService: ProfileService);
    uploadProfile(req: any, createProfileDto: CreateProfileDto, files: {
        imageData?: Express.Multer.File[];
    }): Promise<{
        success: boolean;
        message: string;
        data: {
            email: string;
            bio: string;
            genre: string;
            description: string;
        };
    }>;
    updateProfileByEmail(email: string, createProfileDto: CreateProfileDto, files: {
        imageData?: Express.Multer.File[];
    }): Promise<{
        success: boolean;
        message: string;
    }>;
    findByArtist(email: string): Promise<Profile>;
    findProfileWithSongs(email: string): Promise<any>;
}
