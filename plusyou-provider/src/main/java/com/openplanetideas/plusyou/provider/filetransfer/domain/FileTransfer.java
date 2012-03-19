/*
 * Copyright (c) 2012, Sony Corporation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the Sony Corporation.
 */

package com.openplanetideas.plusyou.provider.filetransfer.domain;

import com.openplanetideas.plusyou.provider.domain.common.AbstractEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
@Entity(name = "file_transfers")
public class FileTransfer extends AbstractEntity {

    private static final long serialVersionUID = 2882950332667734493L;

    @Column(unique = true, nullable = false)
    private String host;
    
    @Column(nullable = false)
    private Integer port;
    
    @Column(nullable = false)
    private String user;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String directory;
    
    @Column(nullable = false)
    private String contactPerson;

    @Column(nullable = false)
    private boolean enabled;

    private FileTransfer() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        FileTransfer fileTransfer = (FileTransfer) obj;
        return new EqualsBuilder()
                .append(host, fileTransfer.getHost())
                .append(port, fileTransfer.getPort())
                .append(user, fileTransfer.getUser())
                .append(directory, fileTransfer.getDirectory())
                .append(contactPerson, fileTransfer.getContactPerson())
                .append(enabled, fileTransfer.isEnabled())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(host)
                .append(port)
                .append(user)
                .append(directory)
                .append(contactPerson)
                .append(enabled)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(host)
                .append(port)
                .append(user)
                .append(directory)
                .append(contactPerson)
                .append(enabled)
                .toString();
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getDirectory() {
        return directory;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public Integer getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    @XmlTransient
    public static class Builder implements org.apache.commons.lang3.builder.Builder<FileTransfer> {

        private FileTransfer fileTransfer = new FileTransfer();
        
        public Builder withHost(String host) {
            fileTransfer.host = host;
            return this;
        }
        
        public Builder withPort(Integer port) {
            fileTransfer.port = port;
            return this;
        }
        
        public Builder withUser(String user) {
            fileTransfer.user = user;
            return this;
        }
        
        public Builder withPassword(String password) {
            fileTransfer.password = password;
            return this;
        }
        
        public Builder withDirectory(String directory) {
            fileTransfer.directory = directory;
            return this;
        }
        
        public Builder withContactPerson(String contactPerson) {
            fileTransfer.contactPerson = contactPerson;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            fileTransfer.enabled = enabled;
            return this;
        }

        @Override
        public FileTransfer build() {
            return fileTransfer;
        }
    }
}