package cn.sparrowmini.bpm.ext;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.appformer.maven.integration.MavenRepository;
import org.appformer.maven.support.AFReleaseId;
import org.appformer.maven.support.AFReleaseIdImpl;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.services.api.DeploymentService;
import org.jbpm.services.api.model.DeploymentUnit;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.services.api.KieServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DeploymentExtServiceImpl implements DeploymentExtService {

    @Autowired
    private DeploymentService deploymentService;

    @Autowired
    private KieServer kieServer;

    @Override
    public AFReleaseId deployKjar(MultipartFile[] files) {

        MavenRepository mavenRepository = MavenRepository.getMavenRepository();
        AFReleaseId releaseId = null;
        MavenXpp3Reader reader = new MavenXpp3Reader();

        try {
            byte[] jarContent = null;
            byte[] pomContent = null;
            for (MultipartFile file : files) {
                if (file.getOriginalFilename().endsWith(".xml")) {
                    pomContent = file.getBytes();
                    Model model = reader.read(file.getInputStream(), false);
                    releaseId = new AFReleaseIdImpl(model.getGroupId(), model.getArtifactId(), model.getVersion());

                }

                if (file.getOriginalFilename().endsWith(".jar")) {
                    jarContent = file.getBytes();
                }
            }

            mavenRepository.installArtifact(releaseId, jarContent, pomContent);

            //deploy
//            DeploymentUnit deploymentUnit = new KModuleDeploymentUnit(releaseId.getGroupId(), releaseId.getArtifactId(), releaseId.getVersion());
//            if (this.deploymentService.isDeployed(deploymentUnit.getIdentifier())) {
//                this.deploymentService.undeploy(deploymentUnit);
//            }
//            this.deploymentService.deploy(deploymentUnit);
            this.kieServer.createContainer(releaseId.getArtifactId() + "-" + releaseId.getVersion(), new KieContainerResource(new ReleaseId(releaseId.getGroupId(), releaseId.getArtifactId(), releaseId.getVersion())));
            return releaseId;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        }

    }
}
