package fr.iglee42.techresourcesgenerator.customize.custompack;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryPack implements PackResources {
    private final Path path;

    public InMemoryPack(Path path) {
        //TechResourcesGenerator.transferTextures();
        this.path = path;
        TechResourcesGenerator.generateData();
    }


    private static String getFullPath(PackType type, ResourceLocation location) {
        return String.format("%s/%s/%s", type.getDirectory(), location.getNamespace(), location.getPath());
    }
    @Nullable
    @Override
    public InputStream getRootResource(String fileName) throws IOException {
        Path resolved = path.resolve(fileName);
        return Files.newInputStream(resolved);
    }

    @Override
    public InputStream getResource(PackType type, ResourceLocation location) throws IOException {
        Path resolved = path.resolve(getFullPath(type, location));
        if (!Files.exists(resolved)){
            throw new IOException("Resource does not exist");
        }
        return Files.newInputStream(resolved);
    }


    @Override
    public Collection<ResourceLocation> getResources(PackType type, String namespaceIn, String pathIn,int unknow,  Predicate<String> filterIn) {
        List<ResourceLocation> result = new ArrayList<>();
        getChildResourceLocations(result, 0, 500, filterIn, path.resolve(type.getDirectory() + "/" + namespaceIn + "/" + pathIn), namespaceIn, pathIn);
        return result;
    }

    private void getChildResourceLocations(List<ResourceLocation> result, int depth, int maxDepth, Predicate<String> filter, Path current, String currentRLNS, String currentRLPath) {
        if (depth >= maxDepth) {
            return;
        }
        try {
            if (!Files.exists(current) || !Files.isDirectory(current)){
                return;
            }
            Stream<Path> list = Files.list(current);
            for (Path child : list.collect(Collectors.toList())) {
                if (!Files.isDirectory(child)) {
                    result.add(new ResourceLocation(currentRLNS, currentRLPath + "/" + child.getFileName()));
                    continue;
                }
                getChildResourceLocations(result, depth + 1, maxDepth, filter, child, currentRLNS, currentRLPath + "/" + child.getFileName());
            }
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }
    @Override
    public boolean hasResource(PackType type, ResourceLocation location) {
        Path finalPath = path.resolve(type.getDirectory() + "/" + location.getNamespace() + "/" + location.getPath());
        return Files.exists(finalPath);
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        Set<String> result = new HashSet<>();
        try {
            Stream<Path> list = Files.list(path.resolve(type.getDirectory()));
            for (Path resultingPath : list.collect(Collectors.toList())) {
                result.add(resultingPath.getFileName().toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> deserializer) throws IOException {
        JsonObject jsonobject = new JsonObject();
        JsonObject packObject = new JsonObject();
        packObject.addProperty("pack_format", 8);
        packObject.addProperty("description", "techresourcescrystal");
        jsonobject.add("pack", packObject);
        if (!jsonobject.has(deserializer.getMetadataSectionName())) {
            return null;
        } else {
            try {
                return deserializer.fromJson(jsonobject.get(deserializer.getMetadataSectionName()).getAsJsonObject());
            } catch (JsonParseException jsonparseexception) {
                return null;
            }
        }
    }

    @Override
    public String getName() {
        return "TRC InCode Pack";
    }

    @Override
    public void close() {

    }
}