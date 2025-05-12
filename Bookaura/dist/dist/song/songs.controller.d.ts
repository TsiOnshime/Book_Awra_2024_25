import { Response } from 'express';
import { SongsService } from './songs.service';
import { CreateSongDto } from 'src/dto/create-songeDto';
export declare class SongsController {
    private readonly songsService;
    constructor(songsService: SongsService);
    uploadSong(createSongDto: CreateSongDto, files: {
        song?: Express.Multer.File[];
        image?: Express.Multer.File[];
    }): Promise<{
        success: boolean;
        message: string;
        data: {
            id: unknown;
            title: string;
            artist: string;
        };
    }>;
    getAllSongs(): Promise<import("../schemas/song.schema").Song[]>;
    streamSong(id: string, res: Response): Promise<void>;
    getImage(id: string, res: Response): Promise<void>;
    getSongsByTitle(title: string): Promise<import("../schemas/song.schema").Song[]>;
    getSongsByArtist(artist: string): Promise<import("../schemas/song.schema").Song[]>;
    getSongById(id: string): Promise<import("../schemas/song.schema").Song>;
}
