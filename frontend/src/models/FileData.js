export default class FileData {
    constructor(id = null,
                fileName = '',
                fileExtension = '',
                mimetype = null,
                fileSize = null,
                relId = null,
    ) {
        this.id = id;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.mimetype = mimetype;
        this.fileSize = fileSize;
        this.relId = relId;
    }
}
