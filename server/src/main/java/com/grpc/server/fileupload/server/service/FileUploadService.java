package com.grpc.server.fileupload.server.service;

import com.devProblems.*;
import com.grpc.server.fileupload.server.utils.DiskFileStorage;
import com.shared.proto.Constants;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.io.IOException;

/**
 * @author A.Sarang Kumar Tak
 * @youtubechannelname Dev Problems
 */
@Slf4j
@GrpcService
public class FileUploadService extends FileUploadServiceGrpc.FileUploadServiceImplBase {

    @Override
    public StreamObserver<FileUploadRequest> uploadFile(StreamObserver<FileUploadResponse> responseObserver) {
        FileMetadata fileMetadata = Constants.fileMetaContext.get();
        DiskFileStorage diskFileStorage = new DiskFileStorage();
        return new StreamObserver<>() {
            @Override
            public void onNext(FileUploadRequest fileUploadRequest) {
                log.info(String.format("received %d length of data", fileUploadRequest.getFile().getContent().size()));
                try {
                    fileUploadRequest.getFile().getContent()
                            .writeTo(diskFileStorage.getStream());
                } catch (IOException e) {
                    responseObserver.onError(io.grpc.Status.INTERNAL
                            .withDescription("cannot write data due to : " + e.getMessage())
                            .asRuntimeException());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.warn("{}", throwable.toString());
            }

            @Override
            public void onCompleted() {

                try {
                    int totalBytesReceived = diskFileStorage.getStream().size();
                    if (totalBytesReceived == fileMetadata.getContentLength()) {
                        diskFileStorage.write(fileMetadata.getFileNameWithType());
                        diskFileStorage.close();
                    } else {
                        responseObserver.onError(
                                io.grpc.Status.FAILED_PRECONDITION
                                        .withDescription(String.format("expected %d but received %d", fileMetadata.getContentLength(), totalBytesReceived))
                                        .asRuntimeException()
                        );
                        return;
                    }
                } catch (IOException e) {
                    responseObserver.onError(io.grpc.Status.INTERNAL
                            .withDescription("cannot save data due to : " + e.getMessage())
                            .asRuntimeException());
                    return;
                }

                responseObserver.onNext(
                        FileUploadResponse
                                .newBuilder()
                                .setFileName(fileMetadata.getFileNameWithType())
                                .setUploadStatus(UploadStatus.SUCCESS)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
    }
}
