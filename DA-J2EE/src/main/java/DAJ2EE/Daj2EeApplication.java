package DAJ2EE;

import DAJ2EE.entity.Permission;
import DAJ2EE.entity.Role;
import DAJ2EE.entity.RolePermission;
import DAJ2EE.Service.PermissionService;
import DAJ2EE.Service.RolePermissionService;
import DAJ2EE.Service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@SpringBootApplication
public class Daj2EeApplication {

	public static void main(String[] args) {
		SpringApplication.run(Daj2EeApplication.class, args);
	}

	@Bean
	public CommandLineRunner fixDatabaseSchema(DataSource dataSource) {
		return args -> {
			try (Connection conn = dataSource.getConnection();
				 Statement stmt = conn.createStatement()) {
				stmt.execute("ALTER TABLE products MODIFY image_url LONGTEXT");
				System.out.println("✓ Database schema migration complete: products.image_url → LONGTEXT");
			} catch (Exception e) {
				System.out.println("⚠ Schema migration skipped or already applied: " + e.getMessage());
			}
		};
	}

	@Bean
	public CommandLineRunner initializeRolesAndPermissions(RoleService roleService,
		PermissionService permissionService,
		RolePermissionService rolePermissionService) {
		return args -> {
			Role userRole = roleService.findByCode("USER").orElseGet(() -> {
				Role r = new Role();
				r.setName("User");
				r.setCode("USER");
				return roleService.save(r);
			});

			Role adminRole = roleService.findByCode("ADMIN").orElseGet(() -> {
				Role r = new Role();
				r.setName("Admin");
				r.setCode("ADMIN");
				return roleService.save(r);
			});

			Permission manageAll = new Permission();
			manageAll.setName("Manage all");
			manageAll.setCode("MANAGE_ALL");

			Permission browseProducts = new Permission();
			browseProducts.setName("Browse products");
			browseProducts.setCode("BROWSE_PRODUCTS");

			Permission createOrders = new Permission();
			createOrders.setName("Create orders");
			createOrders.setCode("CREATE_ORDERS");

			Permission viewProfile = new Permission();
			viewProfile.setName("View profile");
			viewProfile.setCode("VIEW_PROFILE");

			List<Permission> defaultPermissions = List.of(manageAll, browseProducts, createOrders, viewProfile);

			java.util.Map<String, Permission> permissionByCode = new java.util.HashMap<>();
			for (Permission permission : defaultPermissions) {
				String code = permission.getCode();
				Permission saved = permissionService.findByCode(code)
					.orElseGet(() -> permissionService.save(permission));
				permissionByCode.put(code, saved);
			}

			java.util.Set<String> adminPermissions = java.util.Set.of("MANAGE_ALL", "BROWSE_PRODUCTS", "CREATE_ORDERS", "VIEW_PROFILE");
			java.util.Set<String> userPermissions = java.util.Set.of("BROWSE_PRODUCTS", "CREATE_ORDERS", "VIEW_PROFILE");

			if (adminRole != null) {
				for (String permCode : adminPermissions) {
					Permission permissionEntity = permissionByCode.get(permCode);
					if (permissionEntity == null) continue;
					if (rolePermissionService.findByRoleIdAndPermissionId(adminRole.getId(), permissionEntity.getId()).isEmpty()) {
						RolePermission rp = new RolePermission();
						rp.setRole(adminRole);
						rp.setPermission(permissionEntity);
						rp.setIsEnabled(true);
						rolePermissionService.save(rp);
					}
				}
			}

			if (userRole != null) {
				for (String permCode : userPermissions) {
					Permission permissionEntity = permissionByCode.get(permCode);
					if (permissionEntity == null) continue;
					if (rolePermissionService.findByRoleIdAndPermissionId(userRole.getId(), permissionEntity.getId()).isEmpty()) {
						RolePermission rp = new RolePermission();
						rp.setRole(userRole);
						rp.setPermission(permissionEntity);
						rp.setIsEnabled(true);
						rolePermissionService.save(rp);
					}
				}
			}

		};
	}

}
