/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.document.scanner.it.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import richtercloud.document.scanner.components.annotations.CommunicationTree;
import richtercloud.document.scanner.components.annotations.OCRResult;
import richtercloud.document.scanner.components.annotations.ScanResult;
import richtercloud.document.scanner.components.annotations.Tags;
import richtercloud.document.scanner.ifaces.ImageWrapper;
import richtercloud.document.scanner.model.WorkflowItem;

/**
 *
 * @author richter
 */
@Entity
public class DocumentScannerExtensionsTestClass implements Serializable {
    private static final long serialVersionUID = 1L;
    @OCRResult
    private String a;
    @ScanResult
    private List<ImageWrapper> b;
    @CommunicationTree
    private List<WorkflowItem> c = new LinkedList<>();
    @Tags
    private Set<String> d = new HashSet<>();
    @Id
    private Long id;

    public DocumentScannerExtensionsTestClass() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
