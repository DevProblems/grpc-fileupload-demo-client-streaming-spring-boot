package com.grpc.server.fileupload.server.interceptor;

import com.devProblems.FileMetadata;
import com.google.protobuf.InvalidProtocolBufferException;
import com.shared.proto.Constants;
import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

/**
 * @author A.Sarang Kumar Tak
 * @youtubechannelname Dev Problems
 */
@GrpcGlobalServerInterceptor
public class FileUploadInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        FileMetadata fileMetadata = null;
        if (metadata.containsKey(Constants.fileMetadataKey)) {
            byte[] metaBytes = metadata.get(Constants.fileMetadataKey);
            try {
                fileMetadata = FileMetadata.parseFrom(metaBytes);
            } catch (InvalidProtocolBufferException e) {
                Status status = Status.INTERNAL.withDescription("unable to create file metadata");
                serverCall.close(status, metadata);
            }
            Context context = Context.current().withValue(
                    Constants.fileMetaContext,
                    fileMetadata
            );
            return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
        }
        return new ServerCall.Listener<>() {
        };
    }
}
