/*
 * Copyright 2018 NY <nyssance@icloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package genos.models;

import com.google.gson.annotations.SerializedName;

public class UploadParams {
    @SerializedName("key")
    public String key;
    @SerializedName("AWSAccessKeyId")
    public String AWSAccessKeyId;
    @SerializedName("acl")
    public String acl;
    @SerializedName("success_action_status")
    public String successActionStatus;
    @SerializedName("Content-Type")
    public String contentType;
    @SerializedName("Content-Encoding")
    public String contentEncoding;
    @SerializedName("policy")
    public String policy;
    @SerializedName("signature")
    public String signature;

    @Override
    public String toString() {
        return "key=" + key + "&AWSAccessKeyId＝" + AWSAccessKeyId + "&acl=" + acl + "&success_action_status＝"
                + successActionStatus + "&Content-Type" + contentType + "&Content-Encoding" + contentEncoding
                + "&policy" + policy + "&signature＝" + signature;
    }
}
