import { Model } from 'mongoose';
import { Profile } from 'src/schemas/profile.schema';
import { CreateProfileDto } from "../dto/create-profile.dto";
import { SongsService } from "../song/songs.service";
export declare class ProfileService {
    private readonly profileModel;
    private readonly songService;
    constructor(profileModel: Model<Profile>, songService: SongsService);
    create(data: CreateProfileDto & {
        email: string;
        imageData: Buffer;
        imageContentType: string;
    }): Promise<Profile>;
    findByArtist(artist: string): Promise<Profile>;
    updateProfileByEmail(email: string, data: CreateProfileDto & {
        imageData: Buffer;
        imageContentType: string;
    }): Promise<Profile>;
    findProfileWithSongs(artist: string): Promise<any>;
}
