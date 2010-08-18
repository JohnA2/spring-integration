/*
 * Copyright 2010 the original author or authors
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.springframework.integration.sftp;

import com.jcraft.jsch.ChannelSftp;
import org.springframework.util.Assert;

import java.util.*;


/**
 * Patterned very much on the {@link org.springframework.integration.file.CompositeFileListFilter}
 *
 * @author Josh Long
 */
public class CompositeFTPFileListFilter implements SFTPFileListFilter {
    private Set<SFTPFileListFilter> filters;

    public CompositeFTPFileListFilter(SFTPFileListFilter... ftpFileListFilter) {
        this.filters = new LinkedHashSet<SFTPFileListFilter>(Arrays.asList(ftpFileListFilter));
    }

    public CompositeFTPFileListFilter(Collection<SFTPFileListFilter> ftpFileListFilter) {
        this.filters = new LinkedHashSet<SFTPFileListFilter>(ftpFileListFilter);
    }

    public void addFilter(SFTPFileListFilter ftpFileListFilter) {
        this.filters.add(ftpFileListFilter);
    }

    public List<ChannelSftp.LsEntry> filterFiles(ChannelSftp.LsEntry[] files) {
        Assert.notNull(files, "files[] can't be null!");

        List<ChannelSftp.LsEntry> leftOver = Arrays.asList(files);

        for (SFTPFileListFilter ff : this.filters)
            leftOver = ff.filterFiles(leftOver.toArray(new ChannelSftp.LsEntry[leftOver.size()]));

        return leftOver;
    }
}
