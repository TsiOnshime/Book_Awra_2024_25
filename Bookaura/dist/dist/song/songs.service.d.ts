import { Model } from 'mongoose';
import { Song } from 'src/schemas/song.schema';
import { CreateSongDto } from 'src/dto/create-songeDto';
export declare class SongsService {
    private readonly songModel;
    constructor(songModel: Model<Song>);
    create(data: CreateSongDto & {
        songData: Buffer;
        songContentType: string;
        imageData: Buffer;
        imageContentType: string;
    }): Promise<Song>;
    findAll(): Promise<Song[]>;
    findById(id: string): Promise<Song>;
    findByTitle(title: string): Promise<Song[]>;
    findByArtist(artist: string): Promise<Song[]>;
    getSongData(id: string): Promise<{
        data: Buffer;
        contentType: string;
    }>;
    getImageData(id: string): Promise<{
        data: Buffer;
        contentType: string;
    }>;
}
